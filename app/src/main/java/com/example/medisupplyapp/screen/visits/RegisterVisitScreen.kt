package com.example.medisupplyapp.screen.visits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.CustomDropdown
import com.example.medisupplyapp.components.CustomTextArea
import com.example.medisupplyapp.components.ProductSelector
import com.example.medisupplyapp.components.SimpleTopBar
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun RegisterVisitScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onNavigateDetail: (String) -> Unit,
) {
    val viewModel: RegisterVisitViewModel = viewModel()

    val clientOptions = viewModel.clients
    val selectedClient = viewModel.selectedClient
    val selectedDate = viewModel.selectedDate
    val clientError = viewModel.clientError
    val dateError = viewModel.dateError


    var isDatePickerVisible by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    val selectedRoute = "visits"
    var comment by remember { mutableStateOf("") }


    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.visits),
                    onBack = onBack
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))



                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (viewModel.isFormValid()) {
                                showConfirmation = true
                            }
                        },
                        enabled = viewModel.isFormValid(),
                        modifier = Modifier
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    ) {
                        Text(text = stringResource(R.string.register_visit), fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FooterNavigation(selectedRoute, onNavigate)
                }
            },
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                CustomDropdown(
                    label = stringResource(R.string.label_client),
                    options = clientOptions.map { it.fullName },
                    selected = selectedClient?.fullName ?: "",
                    onSelect = { fullName ->
                        val client = clientOptions.find { it.fullName == fullName }
                        viewModel.selectedClient = client
                        viewModel.clientError = false
                    },
                    placeholder = stringResource(R.string.label_client_placeholder),
                    hasError = clientError,
                    onDismissWithoutSelection = {
                        viewModel.validateClientOnDismiss()
                    },
                    enableSearch = true
                )

                Spacer(modifier = Modifier.height(32.dp))


                DateSelector(
                    label = stringResource(R.string.date_of_visit),
                    selectedDate = selectedDate,
                    isError = dateError,
                    onClicked = { isDatePickerVisible = true }
                )
                if (dateError) {
                    Text(
                        text = "Campo obligatorio",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                CustomTextArea(
                    label = stringResource(R.string.observations_label),
                    placeholder = stringResource(R.string.observations_placeholder),
                    value = comment,
                    onValueChange = { comment = it },
                )
            }
        }
    }
    if (isDatePickerVisible) {
        RealDatePickerDialog(
            onDismiss = { isDatePickerVisible = false },
            onDateSelected = { date ->
                viewModel.onDateSelected(date) // Llama al ViewModel con la fecha
                isDatePickerVisible = false
            }
        )
    }
}

@Composable
fun DateSelector(
    label: String,
    selectedDate: Date?,
    isError: Boolean,
    onClicked: () -> Unit
) {
    // Usamos la fecha seleccionada del estado si existe
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateText = selectedDate?.let { dateFormat.format(it) } ?: "Seleccionar Fecha"
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = dateText,
            onValueChange = { /* Solo lectura */ },
            label = { Text("Fecha") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Seleccionar fecha",
                    modifier = Modifier.clickable(onClick = onClicked),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = isError,
            modifier = Modifier
                .clickable(onClick = onClicked)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondary,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
            ),

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    // 1. Crear el estado del DatePicker
    val datePickerState = rememberDatePickerState(
         System.currentTimeMillis()
    )

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
                Text("Cancelar", color = MaterialTheme.colorScheme.primary)
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