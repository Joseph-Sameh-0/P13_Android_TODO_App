package com.example.p13_depi_android_task

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p13_depi_android_task.databinding.FragmentListBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    var myList: MutableList<TODO> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        myList = getFromSharedPreferences("TodoList")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.MyList.layoutManager = LinearLayoutManager(requireContext())
        lateinit var todoAdapter: TODOAdapter

        todoAdapter = TODOAdapter(onItemClick = {
            findNavController().navigate(
                R.id.addTodo, bundleOf(
                    "id" to it.id, "title" to it.title, "description" to it.description
                )
            )
        }, onItemDelete = {
            myList.remove(it)
            todoAdapter.setTODOList(myList)
            saveToSharedPreferences("TodoList", myList)
        })
        todoAdapter.setTODOList(myList)
        binding.MyList.adapter = todoAdapter

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_fromListToAdd)
        }
    }

    private fun saveToSharedPreferences(key: String, list: MutableList<TODO>) {
        val gson = Gson()
        val jsonString = gson.toJson(list)

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, jsonString)
        editor.apply()
    }

    private fun getFromSharedPreferences(key: String): MutableList<TODO> {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(key, null)

        val todoList: MutableList<TODO> = if (jsonString != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<TODO>>() {}.type
            gson.fromJson<MutableList<TODO>>(jsonString, type)
        } else {
            mutableListOf()
        }
        TODO.currentId = if (todoList.isNotEmpty()) {
            todoList.maxOf { it.id }
        } else {
            0
        }
        return todoList
    }

}

