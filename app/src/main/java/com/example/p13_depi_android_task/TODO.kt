package com.example.p13_depi_android_task

data class TODO(
    val id: Int = generateId(),
    val title: String,
    val description: String
){
    companion object {
        private var currentId: Int = 0

        private fun generateId(): Int {
            return ++currentId
        }
    }
}



