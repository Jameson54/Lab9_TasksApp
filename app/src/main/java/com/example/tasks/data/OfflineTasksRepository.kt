package com.example.tasks.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tasks.model.SortOrder
import com.example.tasks.model.TaskSettings
import com.example.tasks.model.sortTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.IOException
class OfflineTasksRepository(
    private val taskDao: TaskDao,
    private val dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>
) : TasksRepository {

    private companion object {
        val LAST_SELECTED_DATE = stringPreferencesKey("last_selected_date")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    override val settingsFlow: Flow<TaskSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(androidx.datastore.preferences.core.emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            TaskSettings(
                selectedDate = preferences[LAST_SELECTED_DATE] ?: "",
                sortOrder = SortOrder.from(preferences[SORT_ORDER])
            )
        }

    override fun getTasksStream(): Flow<List<Task>> =
        combine(taskDao.getAllTasks(), settingsFlow) { tasks, settings ->
            sortTasks(tasks, settings.sortOrder)
        }

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.insert(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun saveSelectedDate(date: String) {
        dataStore.edit { preferences -> preferences[LAST_SELECTED_DATE] = date }
    }

    override suspend fun saveSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences -> preferences[SORT_ORDER] = sortOrder.name }
    }
}
