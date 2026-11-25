package com.example.tasks.ui.entry

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialogHost(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )
    TimePickerDialog(
        title = { Text("Выбор времени") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(state.hour, state.minute)
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    ) {
        TimePicker(state = state)
    }
}
