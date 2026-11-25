package com.example.tasks.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class AppContainer(private val context: Context) {

    private val dataStore: DataStore<Preferences> = context.tasksDataStore

    val tasksRepository: TasksRepository by lazy {
        OfflineTasksRepository(
            taskDao = TaskDatabase.getDatabase(context).taskDao(),
            dataStore = dataStore
        )
    }
}
