package com.example.tasks.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.model.formatShortDate
import com.example.tasks.model.formatTime
import com.example.tasks.ui.AppViewModelProvider
import com.example.tasks.ui.home.DatePickerDialogHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState
    val coroutineScope = rememberCoroutineScope()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(
                            if (viewModel.isNew) R.string.new_task else R.string.edit_task
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp)
                .fillMaxWidth().verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.updateUiState(uiState.copy(title = it)) },
                label = { Text(stringResource(R.string.task_title)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.updateUiState(uiState.copy(description = it)) },
                label = { Text(stringResource(R.string.task_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text("${stringResource(R.string.task_date)}: ${formatShortDate(uiState.date)}")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showTimePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text("${stringResource(R.string.task_time)}: ${formatTime(uiState.hour, uiState.minute)}")
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveTask()
                        navigateBack()
                    }
                },
                enabled = viewModel.isValid(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialogHost(
            initialDate = uiState.date,
            onDateSelected = { viewModel.updateUiState(uiState.copy(date = it)) },
            onDismiss = { showDatePicker = false }
        )
    }
    if (showTimePicker) {
        TimePickerDialogHost(
            initialHour = uiState.hour,
            initialMinute = uiState.minute,
            onTimeSelected = { hour, minute ->
                viewModel.updateUiState(uiState.copy(hour = hour, minute = minute))
            },
            onDismiss = { showTimePicker = false }
        )
    }
}
