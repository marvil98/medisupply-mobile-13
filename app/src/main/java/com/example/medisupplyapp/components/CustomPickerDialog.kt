package com.example.medisupplyapp.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.medisupplyapp.R
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    // 1. Crear el estado del DatePicker
    val datePickerState = rememberDatePickerState()

    // 2. Crear el DatePickerDialog
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Convertir milisegundos a objeto Date y pasarlo al callback
                        onDateSelected(Date(millis))
                    }
                    onDismiss()
                },
                // Habilitar el botón si hay una fecha seleccionada
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel), color = MaterialTheme.colorScheme.primary)
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = Color.White,
            // Aquí puedes personalizar los colores para que coincidan exactamente con la imagen
        )
    ) {
        // 3. Colocar el DatePicker dentro del Dialog
        DatePicker(
            state = datePickerState,
            // Personalización visual para que se parezca más a la imagen
            colors = DatePickerDefaults.colors(
                todayContentColor = MaterialTheme.colorScheme.primary,
                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                selectedDayContentColor = Color.White,
                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                currentYearContentColor = MaterialTheme.colorScheme.primary,
                dayContentColor = Color.Black
            )
        )
    }
}