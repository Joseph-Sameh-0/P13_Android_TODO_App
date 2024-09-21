package com.example.todo.core

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TODO::class], version = 1)
abstract class TODODatabase:RoomDatabase() {

    abstract fun todoDao():TODODao

    companion object{
        private var instance: TODODatabase?=null
        fun getDatabase(context: Context): TODODatabase {
            return instance ?: synchronized(this){
                val localInstance= Room.databaseBuilder(
                    context.applicationContext,
                    TODODatabase::class.java,
                    "Todo_Database"
                ).fallbackToDestructiveMigration().build()
                instance = localInstance
                localInstance
            }
        }
    }

}