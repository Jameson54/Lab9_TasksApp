package com.example.tasks.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tasks.TasksApplication
import com.example.tasks.ui.details.TaskDetailsViewModel
import com.example.tasks.ui.entry.TaskEntryViewModel
import com.example.tasks.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                tasksApplication().container.tasksRepository
            )
        }
        initializer {
            TaskEntryViewModel(
                this.createSavedStateHandle(),
                tasksApplication().container.tasksRepository
            )
        }
        initializer {
            TaskDetailsViewModel(
                this.createSavedStateHandle(),
                tasksApplication().container.tasksRepository
            )
        }
    }
}

fun CreationExtras.tasksApplication(): TasksApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TasksApplication
