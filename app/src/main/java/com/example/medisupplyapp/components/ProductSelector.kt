package com.example.medisupplyapp.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import com.example.medisupplyapp.R
import com.example.medisupplyapp.data.model.Product
import kotlinx.coroutines.delay

@Composable
fun ProductSelector(
    label: String,
    products: List<Product>,
    selectedQuantities: Map<String, Int>,
    onQuantityChange: (String, Int) -> Unit,
    errorMessage: String? = null,
    placeholder: String? = null,
) {
    val resolvedPlaceholder = placeholder ?: stringResource(R.string.product_placeholder)
    val resolvedErrorMessage = errorMessage ?: stringResource(R.string.required_field)

    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selector by remember { mutableStateOf(false) }
    var isManualClose by remember { mutableStateOf(false) }
    var suppressFocus by remember { mutableStateOf(false) }
    var wasDismissedWithoutSelection by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val density = LocalDensity.current

    var textFieldWidth by remember { mutableStateOf(0) }
    var textFieldHeight by remember { mutableStateOf(0) }

    val totalSelectedProducts = remember(selectedQuantities) {
        selectedQuantities.values.sum()
    }

    val displayText = remember(totalSelectedProducts) {
        if (totalSelectedProducts > 0) {
            products
                .filter { (selectedQuantities[it.productId] ?: 0) > 0 }
                .joinToString { "${it.categoryName} (${selectedQuantities[it.productId]})" }
        } else ""
    }

    LaunchedEffect(suppressFocus) {
        if (suppressFocus) {
            delay(150)
            suppressFocus = false
        }
    }

    LaunchedEffect(isManualClose) {
        if (isManualClose) {
            delay(100)
            isManualClose = false
        }
    }

    LaunchedEffect(selector) {
        if (selector) {
            delay(100)
            selector = false
        }
    }

    val showError = totalSelectedProducts == 0 && wasDismissedWithoutSelection

    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) products
        else products.filter {
            it.sku.contains(searchQuery, ignoreCase = true) ||
                    it.categoryName.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    textFieldWidth = it.size.width
                    textFieldHeight = it.size.height
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        expanded = true
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchQuery.isEmpty() && totalSelectedProducts == 0) {
                                Text(
                                    text = resolvedPlaceholder,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                                )
                            } else if (searchQuery.isEmpty() && totalSelectedProducts > 0) {
                                Text(
                                    text = displayText,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular)),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            if (state.isFocused && !suppressFocus) {
                                expanded = true
                            }
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "Abrir o cerrar lista",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (expanded) 180f else 0f)
                        .clickable {
                            if (expanded || selector) {
                                isManualClose = true
                                expanded = false
                                suppressFocus = true
                                focusManager.clearFocus(force = true)
                            } else {
                                expanded = true
                                selector = false
                                focusRequester.requestFocus()
                            }
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (expanded) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = textFieldHeight),
                    onDismissRequest = {
                        if (!isManualClose) {
                            expanded = false
                            focusManager.clearFocus(force = true)
                            selector = true
                            if (totalSelectedProducts == 0) {
                                wasDismissedWithoutSelection = true
                            }
                        }
                    },
                    properties = PopupProperties(focusable = false)
                ) {
                    Box(
                        modifier = Modifier
                            .width(with(density) { textFieldWidth.toDp() })
                            .background(
                                MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                            )
                            .heightIn(max = 300.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        if (filteredProducts.isEmpty() && searchQuery.isNotBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No hay resultados para '$searchQuery'",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular))
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                            ) {
                                filteredProducts.forEach { product ->
                                    val quantity = selectedQuantities[product.productId] ?: 0
                                    ProductCard(
                                        product = product,
                                        quantity = quantity,
                                        onQuantityChange = { newQty ->
                                            onQuantityChange(product.productId, newQty)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showError) {
            Text(
                text = resolvedErrorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
