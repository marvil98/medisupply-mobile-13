package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import java.util.Locale


@Composable
fun CreateOrderScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onNavigateDetail: (String) -> Unit,
    selectedRoute: String
) {
    val viewModel: CreateOrderViewModel = viewModel()

    val clientOptions = viewModel.clients
    val selectedClient = viewModel.selectedClient
    val clientError = viewModel.clientError
    val products = viewModel.products
    var showConfirmation by remember { mutableStateOf(false) }

    val productPriceMap = remember(products) {
        products.associate { it.productId to it.value }
    }

    var selectedQuantities by remember { mutableStateOf(mapOf<Int, Int>()) }

    val totalAmount = remember(selectedQuantities) {
        selectedQuantities.entries.sumOf { (productId, quantity) ->
            val price = productPriceMap[productId] ?: 0.0
            price * quantity
        }
    }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    var comment by remember { mutableStateOf("") }

    if (showConfirmation) {
        ConfirmOrderScreen(
            selectedClient = selectedClient?.name,
            selectedQuantities = selectedQuantities,
            products = products,
            totalAmount = totalAmount,
            onCancel = {
                showConfirmation = false
            },
            onNavigateDetail = onNavigateDetail
        )
    } else {
        MediSupplyTheme {
            Scaffold(
                topBar = {
                    SimpleTopBar(
                        title = stringResource(R.string.create_order),
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.total),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                            )

                            Text(
                                text = stringResource(R.string.total_label, currencyFormatter.format(totalAmount)),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val productsSelected = selectedQuantities.values.sum() > 0

                                if (viewModel.isFormValid() && productsSelected) {
                                    showConfirmation = true
                                }
                            },
                            enabled = viewModel.isFormValid() && totalAmount > 0,
                            modifier = Modifier
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                                disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                            )
                        ) {
                            Text(text = stringResource(R.string.create_order), fontWeight = FontWeight.Bold)
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

                    ProductSelector(
                        label = stringResource(R.string.product_label),
                        products = products,
                        selectedQuantities = selectedQuantities,
                        onQuantityChange = { id, qty ->
                            selectedQuantities =
                                selectedQuantities.toMutableMap().apply { put(id, qty) }
                        },
                    )

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
    }
}