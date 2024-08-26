package com.example.p13_depi_android_task

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.p13_depi_android_task.databinding.TodoInListBinding


class TODOAdapter(
    val onItemClick: (TODO) -> Unit, val onItemDelete: (TODO) -> Unit
) : RecyclerView.Adapter<TODOAdapter.TODOHolder>() {

    private val todoList: MutableList<TODO> = mutableListOf()

    override fun getItemCount() = todoList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TODOHolder {
        val binding = TodoInListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TODOHolder(binding)
    }

    inner class TODOHolder(val binding: TodoInListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(todo: TODO) {
            binding.todo = todo
            binding.root.setOnClickListener { onItemClick(todo) }
            binding.delete.setOnClickListener { onItemDelete(todo) }
        }
    }

    override fun onBindViewHolder(holder: TODOHolder, position: Int) {
        holder.bindData(todoList[position])
    }

    fun setTODOList(list: List<TODO>) {
        val diffUtil = MyDiffUtil(todoList, list)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        todoList.clear()
        todoList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }
}
