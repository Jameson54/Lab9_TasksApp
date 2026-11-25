package com.example.tasks.ui.entry

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import com.example.tasks.model.todayIso
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class TaskEntryUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val hour: Int = 9,
    val minute: Int = 0
)

fun Task.toEntryUiState(): TaskEntryUiState = TaskEntryUiState(
    id = id, title = title, description = description, date = date, hour = hour, minute = minute
)

fun TaskEntryUiState.toTask(): Task = Task(
    id = id, title = title, description = description, date = date, hour = hour, minute = minute
)

class TaskEntryViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val taskId: Int = savedStateHandle.get<Int>("taskId") ?: -1
    val isNew: Boolean get() = taskId == -1

    var uiState by mutableStateOf(TaskEntryUiState(date = todayIso()))
        private set

    init {
        viewModelScope.launch {
            if (isNew) {
                val savedDate = tasksRepository.settingsFlow.first().selectedDate.ifEmpty { todayIso() }
                uiState = uiState.copy(date = savedDate)
            } else {
                tasksRepository.getTaskStream(taskId)
                    .filterNotNull()
                    .first()
                    .let { uiState = it.toEntryUiState() }
            }
        }
    }

    fun updateUiState(newState: TaskEntryUiState) {
        uiState = newState
    }

    fun isValid(): Boolean = uiState.title.isNotBlank()

    suspend fun saveTask() {
        if (!isValid()) return
        val task = uiState.copy(
            id = if (isNew) 0 else taskId,
            title = uiState.title.trim()
        ).toTask()
        if (isNew) tasksRepository.insertTask(task) else tasksRepository.updateTask(task)
    }
}
