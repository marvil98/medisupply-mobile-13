package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.*
import java.text.*
import java.util.*
import com.example.medisupplyapp.R
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import com.example.medisupplyapp.components.FloatingToast
import com.example.medisupplyapp.components.ToastType

@Composable
fun ConfirmOrderScreen(
    selectedClient: String? = null,
    selectedQuantities: Map<Int, Int>,
    products: List<Product>,
    totalAmount: Double,
    onCancel: () -> Unit,
    onNavigateDetail: (String) -> Unit,
    clientId: Int? = null
) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    val productMap = remember(products) { products.associateBy { it.productId } }

    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }
    val viewModel: CreateOrderViewModel = viewModel()
    var orderSuccess by remember { mutableStateOf(false) }

    if (orderSuccess) {
        LaunchedEffect(Unit) {
            val delaySeconds = 2.0f
            kotlinx.coroutines.delay((delaySeconds * 1000).toLong())
            onNavigateDetail("dashboard")
        }
    }

    val successMessage = stringResource(R.string.order_success)
    val mssgError = stringResource(R.string.order_error)

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.confirm_order),
                    onBack = onCancel
                )
            },
            containerColor = MaterialTheme.colorScheme.onPrimary
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                FloatingToast(
                    message = toastMessage,
                    type = toastType,
                    visible = showToast,
                    onDismiss = { showToast = false }
                )
                val clientLabel = selectedClient?.trim()?.takeIf { it.isNotEmpty() }
                    ?: stringResource(R.string.client_not_selected)

                if (selectedClient != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.client_selected),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = clientLabel,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                selectedQuantities.forEach { (productId, quantity) ->
                    val product = productMap[productId]
                    val price = product?.value ?: 0.0
                    val subtotal = price * quantity

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${product?.name} x $quantity")
                        Text(text = currencyFormatter.format(subtotal))
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = currencyFormatter.format(totalAmount),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Button(
                            onClick = {
                                viewModel.createOrder(
                                    selectedQuantities,
                                    clientId,
                                    onSuccess = { orderId, message ->
                                        toastMessage = successMessage
                                        toastType = ToastType.SUCCESS
                                        showToast = true
                                        orderSuccess = true
                                    },
                                    onError = { errorMessage ->
                                        toastMessage = mssgError
                                        toastType = ToastType.ERROR
                                        showToast = true
                                    }
                                )
                            },
                            modifier = Modifier.height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = stringResource(R.string.confirm_order), fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }
        }
    }
}

