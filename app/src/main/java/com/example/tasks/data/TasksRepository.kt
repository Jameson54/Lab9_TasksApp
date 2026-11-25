package com.example.tasks.data

import com.example.tasks.model.SortOrder
import com.example.tasks.model.TaskSettings
import kotlinx.coroutines.flow.Flow

interface TasksRepository {
    fun getTasksStream(): Flow<List<Task>>
    fun getTaskStream(id: Int): Flow<Task?>
    val settingsFlow: Flow<TaskSettings>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun saveSelectedDate(date: String)
    suspend fun saveSortOrder(sortOrder: SortOrder)
}
