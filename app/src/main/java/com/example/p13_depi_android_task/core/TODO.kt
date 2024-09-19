package com.example.p13_depi_android_task.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class TODO(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var title: String,
    var description: String
)
