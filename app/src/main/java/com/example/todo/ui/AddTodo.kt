package com.example.todo.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.core.TODO
import com.example.todo.core.TODODatabase
import com.example.todo.databinding.FragmentAddTodoBinding
import kotlinx.coroutines.launch

class AddTodo : Fragment() {
    private lateinit var binding: FragmentAddTodoBinding
    private val todoDatabase: TODODatabase by lazy { TODODatabase.getDatabase(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rootLayout = view.findViewById<ConstraintLayout>(R.id.root_layout)
        val fab = view.findViewById<ConstraintLayout>(R.id.floatingActionButtons)

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

        if (arguments?.getInt("id") != null) {
            binding.todo = TODO(
                arguments?.getInt("id")!!,
                arguments?.getString("title") ?: "",
                arguments?.getString("description") ?: ""
            )
            binding.floatingEditButton.visibility = View.VISIBLE
            binding.floatingDeleteButton.visibility = View.VISIBLE
            binding.Title.isEnabled = false
            binding.Description.isEnabled = false
        }
        binding.floatingSaveButton.setOnClickListener {
            if (arguments?.getInt("id") != null) {

                val updatedTodo = TODO(
                    arguments?.getInt("id")!!,
                    title = binding.Title.text.toString(),
                    description = binding.Description.text.toString()
                )
                lifecycleScope.launch {
                    todoDatabase.todoDao().updateTodo(updatedTodo)
                }
            } else {
                val newTodo = TODO(
                    title = binding.Title.text.toString(),
                    description = binding.Description.text.toString()
                )

                lifecycleScope.launch {
                    todoDatabase.todoDao().insertTodo(newTodo)
                }
            }
            findNavController().navigateUp()
        }

        binding.floatingEditButton.setOnClickListener {
            binding.Title.isEnabled = true
            binding.Description.isEnabled = true
            binding.floatingEditButton.visibility = View.GONE
        }

        binding.floatingDeleteButton.setOnClickListener {
            binding.todo?.let {
                lifecycleScope.launch {
                    todoDatabase.todoDao().deleteTodo(it)
                }
            }
            findNavController().navigateUp()
        }

    }
}
