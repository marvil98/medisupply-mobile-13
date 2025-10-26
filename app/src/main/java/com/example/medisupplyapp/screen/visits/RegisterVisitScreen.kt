package com.example.medisupplyapp.screen.visits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import com.example.medisupplyapp.components.FloatingToast
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.components.ToastType
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
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
    val findings = viewModel.findings
    val clientError = viewModel.clientError
    val dateError = viewModel.dateError
    val dateErrorMessage = viewModel.errorMessage

    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }

    val successMessage = stringResource(R.string.visit_success)
    val mssgError = stringResource(R.string.visit_error)

    var isDatePickerVisible by remember { mutableStateOf(false) }

    val selectedRoute = "visits"

    var visitSuccess by remember { mutableStateOf(false) }

    if (visitSuccess) {
        LaunchedEffect(Unit) {
            val delaySeconds = 2.0f
            kotlinx.coroutines.delay((delaySeconds * 1000).toLong())
            onNavigateDetail("dashboard")
        }
    }

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

                    FloatingToast(
                        message = toastMessage,
                        type = toastType,
                        visible = showToast,
                        onDismiss = { showToast = false }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (viewModel.isFormValid()) {
                                viewModel.registerVisit(
                                    onSuccess = { orderId, message ->
                                        toastMessage = successMessage
                                        toastType = ToastType.SUCCESS
                                        showToast = true
                                        visitSuccess = true
                                    },
                                    onError = { errorMessage ->
                                        toastMessage = mssgError
                                        toastType = ToastType.ERROR
                                        showToast = true
                                    }
                                )
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
                    options = clientOptions.map { it.name },
                    selected = selectedClient?.name ?: "",
                    onSelect = { name ->
                        val client = clientOptions.find { it.name == name }
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
                if (dateError && dateErrorMessage != "") {
                    Text(
                        text = dateErrorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                CustomTextArea(
                    label = stringResource(R.string.observations_label),
                    placeholder = stringResource(R.string.observations_placeholder),
                    value = findings,
                    onValueChange = { viewModel.updateFindings(it) },
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
    val dateText = selectedDate?.let { "" } ?: "Seleccionar Fecha"
    // Usamos InteractionSource para capturar el clic sobre el TextField completo
    val interactionSource = remember { MutableInteractionSource() }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                // Aplicamos el clic al Box, que abarca toda el área del TextField.
                // Esto garantiza que el toque se detecte ANTES de la lógica interna del TextField.
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClicked
                )
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            OutlinedTextField(
                // --- Usar la fecha formateada o el placeholder ---
                value = dateText,
                onValueChange = { /* Solo lectura */ },
                readOnly = true, // Es fundamental mantener esto para evitar el teclado
                enabled = false, // Deshabilitar evita que capture eventos de foco/teclado
                label = { Text("Fecha") },
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = isError,
                // El Modifier del TextField ahora solo necesita rellenar el Box padre
                modifier = Modifier.fillMaxSize(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Fondo transparente ya que el Box lo maneja
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    // Usamos el color de texto para el estado 'disabled'
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    errorSupportingTextColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealDatePickerDialog(
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