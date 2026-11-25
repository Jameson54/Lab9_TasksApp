package com.example.tasks.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class TaskDetailsUiState(
    val task: Task = Task(title = "", description = "", date = "", hour = 0, minute = 0)
)

class TaskDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val taskId: Int = checkNotNull(savedStateHandle.get<Int>("taskId"))

    val uiState: StateFlow<TaskDetailsUiState> = tasksRepository.getTaskStream(taskId)
        .filterNotNull()
        .map { TaskDetailsUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = TaskDetailsUiState()
        )

    suspend fun deleteTask() {
        tasksRepository.deleteTask(uiState.value.task)
    }
}
