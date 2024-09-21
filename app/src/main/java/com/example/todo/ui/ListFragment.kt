package com.example.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.core.TODOAdapter
import com.example.todo.core.TODODatabase
import com.example.todo.databinding.FragmentListBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.MyList.layoutManager = LinearLayoutManager(requireContext())
        lateinit var todoAdapter: TODOAdapter

        val todoDatabase: TODODatabase by lazy { TODODatabase.getDatabase(requireActivity().application) }
        lifecycleScope.launch {
            val myList = withContext(Dispatchers.IO) {
                todoDatabase.todoDao().getAllTodo()
            }

            todoAdapter = TODOAdapter(onItemClick = {
                findNavController().navigate(
                    R.id.addTodo, bundleOf(
                        "id" to it.id, "title" to it.title, "description" to it.description
                    )
                )
            }, onItemDelete = { todo ->
                myList.remove(todo)
                todoAdapter.setTODOList(myList)
                lifecycleScope.launch {
                    todoDatabase.todoDao().deleteTodo(todo)
                }
            })
            todoAdapter.setTODOList(myList)
            binding.MyList.adapter = todoAdapter
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_fromListToAdd)
        }
    }
}

