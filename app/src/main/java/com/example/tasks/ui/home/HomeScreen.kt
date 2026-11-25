package com.example.tasks.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasks.R
import com.example.tasks.data.Task
import com.example.tasks.model.SortOrder
import com.example.tasks.model.formatShortDate
import com.example.tasks.model.formatTime
import com.example.tasks.model.formatWeekday
import com.example.tasks.model.shiftDate
import com.example.tasks.model.todayIso
import com.example.tasks.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToTaskDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.homeUiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showAllDates by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.Filled.Sort, contentDescription = stringResource(R.string.sort_title))
                    }
                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOrder.entries.forEach { order ->
                            DropdownMenuItem(
                                text = { Text(order.label) },
                                onClick = {
                                    viewModel.setSortOrder(order)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Filled.CalendarMonth,
                            contentDescription = stringResource(R.string.pick_date)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToTaskEntry) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.new_task))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            DateStrip(
                selectedDate = uiState.selectedDate,
                onSelect = { viewModel.selectDate(it) }
            )

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = showAllDates,
                    onClick = { showAllDates = !showAllDates },
                    label = { Text(stringResource(R.string.all_dates)) }
                )
                SpacerWidth()
                Text(
                    text = "${stringResource(R.string.sort_title)}: ${uiState.sortOrder.label}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            val visibleTasks = if (showAllDates) {
                uiState.tasks
            } else {
                uiState.tasks.filter { it.date == uiState.selectedDate }
            }

            if (visibleTasks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.empty_tasks))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = visibleTasks, key = { it.id }) { task ->
                        TaskListItem(
                            task = task,
                            onClick = { navigateToTaskDetails(task.id) },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialogHost(
            initialDate = uiState.selectedDate.ifEmpty { todayIso() },
            onDateSelected = { viewModel.selectDate(it) },
            onDismiss = { showDatePicker = false }
        )
    }
}

@Composable
private fun SpacerWidth() {
    androidx.compose.foundation.layout.Spacer(Modifier.padding(horizontal = 8.dp))
}

@Composable
private fun DateStrip(selectedDate: String, onSelect: (String) -> Unit) {
    val baseDate = selectedDate.ifEmpty { todayIso() }
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
    ) {
        items(count = 30) { offset ->
            val date = shiftDate(baseDate, offset)
            FilterChip(
                selected = date == selectedDate,
                onClick = { onSelect(date) },
                label = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(formatWeekday(date), style = MaterialTheme.typography.labelSmall)
                        Text(formatShortDate(date), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            )
        }
    }
}

@Composable
private fun TaskListItem(task: Task, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(task.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${formatShortDate(task.date)} · ${formatTime(task.hour, task.minute)}",
                    style = MaterialTheme.typography.bodySmall
                )
                if (task.description.isNotBlank()) {
                    Text(task.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.delete))
            }
        }
    }
}
