package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.*
import com.example.medisupplyapp.data.model.Product
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
    clientId: Int,
    selectedRoute: String
) {
    val viewModel: CreateOrderViewModel = viewModel()
    val products = viewModel.products
    val recommendationState by viewModel.uiState.collectAsState()

    var selectedQuantities by remember { mutableStateOf(mapOf<Int, Int>()) }
    var showConfirmation by remember { mutableStateOf(false) }

    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    val productPriceMap = remember(products) {
        products.associate { it.productId to it.value }
    }

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

    LaunchedEffect(key1 = true) {
        viewModel.loadRecommendations(clientId = clientId)
    }

    fun filterProducts(
        recommended: List<Product>,
        products: List<Product>,
        query: String
    ): Pair<List<Product>, List<Product>> {
        if (query.isBlank()) return recommended to products

        val filteredRecommended = recommended.filter {
            it.name.contains(query, ignoreCase = true)
        }
        val filteredProducts = products.filter {
            it.name.contains(query, ignoreCase = true)
        }
        return filteredRecommended to filteredProducts
    }


    if (showConfirmation) {
        ConfirmOrderScreen(
            selectedQuantities = selectedQuantities,
            products = products,
            totalAmount = totalAmount,
            onCancel = {
                showConfirmation = false
            },
            onNavigateDetail = onNavigateDetail,
            clientId = clientId
        )
    } else {
        MediSupplyTheme {
            Scaffold(
                topBar = { SimpleTopBar(title = stringResource(R.string.orders), onBack = onBack) },
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
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                            )
                            Text(
                                text = currencyFormatter.format(totalAmount),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val productsSelected = selectedQuantities.values.sum() > 0
                                if (productsSelected) {
                                    showConfirmation = true
                                }
                            },
                            enabled = totalAmount > 0,
                            modifier = Modifier.height(48.dp),
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
                        text = stringResource(R.string.create_order),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 🔹 Buscador
                    var searchQuery by remember { mutableStateOf("") }
                    SearchInput(
                        modifier = Modifier.padding(bottom = 16.dp),
                        onSearch = { query -> searchQuery = query },
                        placeholderText = stringResource(R.string.search_placeholder_product)
                    )

                    // 🔹 Filtrar productos
                    val (filteredRecommended, filteredProducts) = filterProducts(
                        (recommendationState as? RecommendationUiState.Success)?.recommendedProducts ?: emptyList(),
                        products,
                        searchQuery
                    )

                    PaginatedProductList(
                        recommended = filteredRecommended,
                        products = filteredProducts,
                        selectedQuantities = selectedQuantities,
                        onQuantityChange = onQuantityChange,
                        pageSize = 3
                    )
                }
            }
        }
    }
}


@Composable
fun PaginatedProductList(
    recommended: List<Product>,
    products: List<Product>,
    selectedQuantities: Map<Int, Int>,
    onQuantityChange: (Int, Int) -> Unit,
    pageSize: Int = 3
) {
    var currentPage by remember { mutableStateOf(0) }

    data class ProductWithBlock(val product: Product, val blockTitle: String)

    val allProducts = buildList {
        recommended.forEach { add(ProductWithBlock(it, stringResource(R.string.your_recommendations))) }
        products.forEach { add(ProductWithBlock(it, stringResource(R.string.products))) }
    }

    val totalPages = (allProducts.size + pageSize - 1) / pageSize
    val startIndex = currentPage * pageSize
    val endIndex = minOf(startIndex + pageSize, allProducts.size)
    val visible = allProducts.subList(startIndex, endIndex)

    Column {
        var lastHeader: String? = null
        visible.forEach { pwb ->
            if (pwb.blockTitle != lastHeader) {
                Text(
                    text = pwb.blockTitle,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                lastHeader = pwb.blockTitle
            }

            val quantity = selectedQuantities[pwb.product.productId] ?: 0
            ProductCard(
                product = pwb.product,
                quantity = quantity,
                onQuantityChange = { newQty ->
                    onQuantityChange(pwb.product.productId, newQty)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (currentPage > 0) currentPage-- },
                enabled = currentPage > 0
            ) {
                Text(stringResource(R.string.previous))
            }

            Text(
                text = stringResource(R.string.page_label, currentPage + 1, totalPages),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { if (currentPage < totalPages - 1) currentPage++ },
                enabled = currentPage < totalPages - 1
            ) {
                Text(stringResource(R.string.next))
            }
        }
    }
}

