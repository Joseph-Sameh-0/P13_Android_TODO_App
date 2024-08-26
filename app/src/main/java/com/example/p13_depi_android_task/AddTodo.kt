package com.example.p13_depi_android_task

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.p13_depi_android_task.databinding.FragmentAddTodoBinding
import com.example.p13_depi_android_task.databinding.FragmentListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddTodo : Fragment() {
    private lateinit var binding: FragmentAddTodoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rootLayout = view.findViewById<RelativeLayout>(R.id.root_layout)
        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)

        rootLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootLayout.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootLayout.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is opened
                fab.animate().translationY(-keypadHeight.toFloat()).setDuration(300).start()
            } else {
                // Keyboard is closed
                fab.animate().translationY(0f).setDuration(300).start()
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val myList: MutableList<TODO> = getFromSharedPreferences("TodoList")
            if (arguments?.getInt("id") != null) {
                myList.remove(
                    TODO(
                        arguments?.getInt("id")!!,
                        arguments?.getString("title").toString(),
                        arguments?.getString("description").toString()
                    )
                )
                myList.add(
                    TODO(
                        arguments?.getInt("id")!!,
                        title = binding.Title.text.toString(),
                        description = binding.Description.text.toString()
                    )
                )
            } else {
                myList.add(
                    TODO(
                        title = binding.Title.text.toString(),
                        description = binding.Description.text.toString()
                    )
                )
            }
            saveToSharedPreferences("TodoList", myList)
            findNavController().navigateUp()
        }

        if (arguments?.getInt("id") != null) {
            binding.todo = TODO(
                arguments?.getInt("id")!!,
                arguments?.getString("title").toString(),
                arguments?.getString("description").toString()
            )
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

        return if (jsonString != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<TODO>>() {}.type
            gson.fromJson<MutableList<TODO>>(jsonString, type)
        } else {
            mutableListOf()
        }
    }

}