package com.example.tasks.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.model.formatShortDate
import com.example.tasks.model.formatTime
import com.example.tasks.ui.AppViewModelProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navigateToEdit: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val task = uiState.task
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(task.title) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(16.dp).fillMaxSize()
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(task.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "${stringResource(R.string.task_date)}: ${formatShortDate(task.date)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        "${stringResource(R.string.task_time)}: ${formatTime(task.hour, task.minute)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(task.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { navigateToEdit(task.id) },
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.edit)) }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteTask()
                        navigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(R.string.delete)) }
        }
    }
}
