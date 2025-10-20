package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.*
import java.text.*
import java.util.*
import com.example.medisupplyapp.R
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun ConfirmOrderScreen(
    selectedClient: String,
    selectedQuantities: Map<String, Int>,
    products: List<Product>,
    totalAmount: Double,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onNavigate: (String) -> Unit = {}
) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val selectedRoute = "orders/confirm"

    val productMap = remember(products) { products.associateBy { it.productId } }

    val displayText = remember(selectedQuantities) {
        products
            .filter { (selectedQuantities[it.productId] ?: 0) > 0 }
            .joinToString { "${it.categoryName} (${selectedQuantities[it.productId]})" }
    }

    val viewModel = remember { CreateOrderViewModel() }

    fun handleOrderConfirm() {
        val selectedProducts = products.filter {
            val qty = selectedQuantities[it.productId] ?: 0
            qty > 0
        }

        viewModel.selectedClient = selectedClient
        viewModel.selectedProducts = selectedProducts

        viewModel.createOrder(
            onSuccess = { orderId, message ->
                onConfirm()
            },
            onError = { error ->
            }
        )
    }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.confirm_order),
                    onBack = onCancel
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
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

                    Spacer(modifier = Modifier.height(16.dp))

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
                            onClick = onConfirm,
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
                Text(
                    text = stringResource(R.string.client_selected, selectedClient),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.product_selected),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

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
                        Text(text = "${product?.value} x$quantity")
                        Text(text = currencyFormatter.format(subtotal))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.total),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

