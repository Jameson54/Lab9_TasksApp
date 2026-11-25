package com.example.tasks

import com.example.tasks.data.Task
import com.example.tasks.model.SortOrder
import com.example.tasks.model.sortTasks
import org.junit.Assert.assertEquals
import org.junit.Test

class TaskSorterTests {

    private val tasks = listOf(
        Task(id = 1, title = "Б", description = "", date = "2026-11-27", hour = 10, minute = 0),
        Task(id = 2, title = "А", description = "", date = "2026-11-26", hour = 9, minute = 30),
        Task(id = 3, title = "В", description = "", date = "2026-11-28", hour = 8, minute = 0)
    )

    @Test
    fun sortByDateAscending() {
        assertEquals(listOf(2, 1, 3), sortTasks(tasks, SortOrder.DATE_ASC).map { it.id })
    }

    @Test
    fun sortByDateDescending() {
        assertEquals(listOf(3, 1, 2), sortTasks(tasks, SortOrder.DATE_DESC).map { it.id })
    }

    @Test
    fun sortByTitleAlphabetically() {
        assertEquals(listOf(2, 1, 3), sortTasks(tasks, SortOrder.TITLE_ASC).map { it.id })
    }
}
