package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.*
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun CreateOrderClientScreen(
    onBack: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onNavigateDetail: (String) -> Unit,
) {
    val viewModel: CreateOrderViewModel = viewModel()
    val products = viewModel.products

    val recommendationState by viewModel.uiState.collectAsState()

    val selectedRoute = "orders/create"

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

    val onQuantityChange: (Int, Int) -> Unit = { productId, newQty ->
        selectedQuantities = if (newQty > 0) {
            selectedQuantities + (productId to newQty)
        } else {
            selectedQuantities - productId
        }
    }

    val handleSearch: (String) -> Unit = { query ->
        println("Iniciando búsqueda de productos")
    }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    LaunchedEffect(key1 = true) {
        viewModel.loadRecommendations(clientId = 100)
    }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.orders),
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
                Text(
                    text = stringResource(R.string.create_order),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.select_products),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.height(16.dp))
                SearchInput(
                    modifier = Modifier.padding(bottom = 16.dp),
                    onSearch = handleSearch,
                    placeholderText = stringResource(R.string.search_placeholder_product)
                )

                Spacer(modifier = Modifier.height(24.dp))

                RecommendationContent(
                    state = recommendationState,
                    selectedQuantities = selectedQuantities,
                    onQuantityChange = onQuantityChange
                )
            }
        }
    }
}

@Composable
fun RecommendationContent(
    state: RecommendationUiState,
    selectedQuantities: Map<Int, Int>,
    onQuantityChange: (Int, Int) -> Unit
) {
    when (state) {
        is RecommendationUiState.Loading -> {
            LoadingScreen()
        }
        is RecommendationUiState.Error -> {
            null
        }
        is RecommendationUiState.Success -> {
            val recommendedProducts = state.recommendedProducts

            if (recommendedProducts.isEmpty()) {
                Text(
                    text = stringResource(R.string.recommend),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            } else {
                recommendedProducts.forEach { product ->
                    val quantity = selectedQuantities[product.productId] ?: 0
                    Text(
                        text = stringResource(R.string.recommendations),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProductCard(
                        product = product,
                        quantity = quantity,
                        onQuantityChange = { newQty ->
                            onQuantityChange(product.productId, newQty)
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}