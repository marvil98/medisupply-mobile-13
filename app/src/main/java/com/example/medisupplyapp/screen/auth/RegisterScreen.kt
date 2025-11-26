package com.example.medisupplyapp.screen.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.FloatingToast
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.components.ToastType
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun ClientRegistrationScreen(viewModel: RegisterViewModel, onNavigateDetail: (String) -> Unit, onBack: (String) -> Unit, ) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val address by viewModel.address
    val latitude by viewModel.latitude
    val longitude by viewModel.longitude
    val uiState by viewModel.uiState.collectAsState()
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }
    var showToast by remember { mutableStateOf(false) }

    val successMessage = stringResource(R.string.user_success)
    val mssgError = stringResource(R.string.user_error)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            viewModel.updateAddress(
                place.address ?: "",
                place.latLng?.latitude ?: 0.0,
                place.latLng?.longitude ?: 0.0
            )
        }
    }

    var userSuccess by remember { mutableStateOf(false) }

    if (userSuccess) {
        LaunchedEffect(Unit) {
            val delaySeconds = 2.0f
            kotlinx.coroutines.delay((delaySeconds * 1000).toLong())
            onNavigateDetail("login")
        }
    }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.register_label),
                    onBack = {onBack("splash")}
                )

            },
            containerColor =MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.welcome),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.name),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.name,
                    onValueChange = { viewModel.updateField("name", it) },
                    placeholder = {
                        Text(
                            "Jhon",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("name"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["name"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.lastname),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.lastname,
                    onValueChange = { viewModel.updateField("lastname", it) },
                    placeholder = {
                        Text(
                            "Doe",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("lastname"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["lastname"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.identification),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.identification,
                    onValueChange = { viewModel.updateField("identification", it) },
                    placeholder = {
                        Text(
                            "Ej: 1234567890",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("identification"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["identification"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.email),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { viewModel.updateField("email", it) },
                    placeholder = {
                        Text(
                            "example@example.com",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("email"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["email"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.label_phone_register),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.phone,
                    onValueChange = { viewModel.updateField("phone", it) },
                    placeholder = {
                        Text(
                            "Ej: 573173493598",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("phone"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["phone"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.password),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { viewModel.updateField("password", it) },
                    placeholder = {
                        Text(
                            "••••••••",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.Visibility
                                } else {
                                    Icons.Default.VisibilityOff
                                },
                                contentDescription = if (passwordVisible) {
                                    stringResource(R.string.show_password)
                                } else {
                                    stringResource(R.string.hide_password)
                                },
                                tint = MaterialTheme.colorScheme.inverseSurface
                            )
                        }
                    },
                    isError = uiState.errors.containsKey("password"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["password"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.data_company),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.nit),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.nit,
                    onValueChange = { viewModel.updateField("nit", it) },
                    placeholder = {
                        Text(
                            "Ej: 900123456-9",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("nit"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["nit"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.company_name),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.companyName,
                    onValueChange = { viewModel.updateField("companyName", it) },
                    placeholder = {
                        Text(
                            "Ej: Cliente Ejemplo S.A.",
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    isError = uiState.errors.containsKey("companyName"),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["companyName"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = address,
                    onValueChange = {  },
                    placeholder = {
                        Text(
                            stringResource(R.string.select_address),
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    shape = RoundedCornerShape(8.dp),
                    isError = uiState.errors.containsKey("address"),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.inverseSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor =MaterialTheme.colorScheme.background
                    ),
                    supportingText = {
                        val errorMessage = uiState.errors["address"]
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    },
                )
                Button(
                    onClick = {
                        val intent = Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.OVERLAY,
                            listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
                        ).build(context)
                        launcher.launch(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(stringResource(R.string.search_address), style = MaterialTheme.typography.bodySmall)
                }

                FloatingToast(
                    message = toastMessage,
                    type = toastType,
                    visible = showToast,
                    onDismiss = { showToast = false }
                )

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (viewModel.validateForm(context)) {
                            viewModel.createUserAndCompany(
                                context,
                                address,
                                latitude,
                                longitude,
                                onSuccess = { orderId, message ->
                                    toastMessage = successMessage
                                    toastType = ToastType.SUCCESS
                                    showToast = true
                                    userSuccess = true
                                },
                                onError = { errorMessage ->
                                    toastMessage = errorMessage
                                    toastType = ToastType.ERROR
                                    showToast = true
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color(0xFFBDBDBD)
                    )
                ) {
                    Text(stringResource(R.string.create_account))
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }

}
