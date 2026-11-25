package com.example.tasks.model

data class TaskSettings(
    val selectedDate: String = "",
    val sortOrder: SortOrder = SortOrder.DATE_ASC
)
