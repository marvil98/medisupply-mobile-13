package com.example.medisupplyapp.screen.visits


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.CustomDropdown
import com.example.medisupplyapp.components.CustomPickerDialog
import com.example.medisupplyapp.components.CustomTextArea
import com.example.medisupplyapp.components.DateSelector
import com.example.medisupplyapp.components.FloatingToast
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.components.ToastType
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme


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
    var showConfirmation by remember { mutableStateOf(false) }
    if (visitSuccess) {
        LaunchedEffect(Unit) {
            val delaySeconds = 2.0f
            kotlinx.coroutines.delay((delaySeconds * 1000).toLong())
            showConfirmation = true
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
                                    onSuccess = { _, _ ->
                                        toastMessage = successMessage
                                        toastType = ToastType.SUCCESS
                                        showToast = true
                                        visitSuccess = true
                                    },
                                    onError = { _ ->
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

                if (isDatePickerVisible) {
                    CustomPickerDialog (
                        onDismiss = { isDatePickerVisible = false },
                        onDateSelected = { date ->
                            viewModel.onDateSelected(date)
                            isDatePickerVisible = false
                        }
                    )
                }

                if (showConfirmation) {
                    AlertDialog(
                        onDismissRequest = { showConfirmation = false },
                        containerColor = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(8.dp),
                        title = {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = { showConfirmation = false }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = stringResource(R.string.close_label),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Text(
                                    text = stringResource(R.string.message_new_evidence),
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                            }
                        },
                        confirmButton = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Button(
                                    onClick = { showConfirmation = false },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Text(stringResource(R.string.message_no), color = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        showConfirmation = false
                                        onNavigate("evidencias/1")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(stringResource(R.string.message_yes), color = Color.White)
                                }
                            }
                        },
                        dismissButton = {}
                    )
                }
            }
        }
    }
}
