package com.example.todo.core

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TODODao {
    @Insert
    suspend fun insertTodo(todo: TODO)

    @Delete
    suspend fun deleteTodo(todo: TODO)

    @Update
    suspend fun updateTodo(todo: TODO)

    @Query("select * from todo_table order by id asc")
    fun getAllTodo():MutableList<TODO>
}