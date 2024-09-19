package com.example.p13_depi_android_task.core

class TODORepo (private val todoDao: TODODao){
    val allTodo:MutableList<TODO> = todoDao.getAllTodo()

    suspend fun insertTodo(todo: TODO){
        todoDao.insertTodo(todo)
    }

    suspend fun updateTodo(todo: TODO){
        todoDao.updateTodo(todo)
    }
    suspend fun deleteTodo(todo: TODO){
        todoDao.deleteTodo(todo)
    }

}