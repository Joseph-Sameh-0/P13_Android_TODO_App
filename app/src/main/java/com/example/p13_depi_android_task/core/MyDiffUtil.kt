package com.example.p13_depi_android_task.core

import androidx.recyclerview.widget.DiffUtil
import com.example.p13_depi_android_task.ui.TODO

class MyDiffUtil(
    val oldList: List<TODO>,
    val newList: List<TODO>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}