package com.example.p13_depi_android_task.core

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

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