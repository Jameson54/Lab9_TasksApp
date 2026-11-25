package com.example.tasks.model

import com.example.tasks.data.Task

internal fun sortTasks(tasks: List<Task>, sortOrder: SortOrder): List<Task> = when (sortOrder) {
    SortOrder.DATE_ASC -> tasks.sortedWith(
        compareBy<Task> { it.date }.thenBy { it.hour }.thenBy { it.minute }.thenBy { it.id }
    )
    SortOrder.DATE_DESC -> tasks.sortedWith(
        compareByDescending<Task> { it.date }.thenBy { it.hour }.thenBy { it.minute }.thenBy { it.id }
    )
    SortOrder.TITLE_ASC -> tasks.sortedWith(
        compareBy<Task, String>(String.CASE_INSENSITIVE_ORDER) { it.title }.thenBy { it.date }
    )
}
