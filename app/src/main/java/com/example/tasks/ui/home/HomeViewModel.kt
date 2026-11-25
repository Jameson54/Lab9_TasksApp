package com.example.tasks.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.Task
import com.example.tasks.data.TasksRepository
import com.example.tasks.model.SortOrder
import com.example.tasks.model.todayIso
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val selectedDate: String = "",
    val sortOrder: SortOrder = SortOrder.DATE_ASC
)

class HomeViewModel(private val tasksRepository: TasksRepository) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = combine(
        tasksRepository.getTasksStream(),
        tasksRepository.settingsFlow
    ) { tasks, settings ->
        HomeUiState(
            tasks = tasks,
            selectedDate = settings.selectedDate.ifEmpty { todayIso() },
            sortOrder = settings.sortOrder
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState()
    )

    fun selectDate(date: String) {
        viewModelScope.launch { tasksRepository.saveSelectedDate(date) }
    }

    fun setSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch { tasksRepository.saveSortOrder(sortOrder) }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch { tasksRepository.deleteTask(task) }
    }
}
