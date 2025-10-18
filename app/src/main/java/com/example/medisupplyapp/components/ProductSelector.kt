package com.example.medisupplyapp.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.example.medisupplyapp.R

@Composable
fun ProductSelector(
    label: String,
    products: List<Product>,
    selectedQuantities: Map<String, Int>,
    onQuantityChange: (String, Int) -> Unit,
    errorMessage: String = stringResource(R.string.required_field),
    placeholder: String = stringResource(R.string.product_placeholder),
) {
    var searchQuery by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var wasDismissedWithoutSelection by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current
    var hasInteracted by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    val totalSelectedProducts = remember(selectedQuantities) {
        selectedQuantities.values.sum()
    }

    val displayText = remember(totalSelectedProducts) {
        if (totalSelectedProducts > 0) {
            "$totalSelectedProducts ${if (totalSelectedProducts == 1) "producto seleccionado" else "productos seleccionados"}"
        } else {
            ""
        }
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            expanded = true
        } else if (!expanded) {
            searchQuery = ""
        }
    }

    LaunchedEffect(expanded) {
        if (!expanded) {
            focusRequester.freeFocus()
            focusManager.clearFocus()

            if (hasInteracted) {
                if (selectedQuantities.values.sum() == 0) {
                    wasDismissedWithoutSelection = true
                } else {
                    wasDismissedWithoutSelection = false
                }
            }
        } else {
            hasInteracted = true
        }
    }

    LaunchedEffect(totalSelectedProducts) {
        if (totalSelectedProducts > 0) {
            wasDismissedWithoutSelection = false
        }
    }

    val showError = totalSelectedProducts == 0 && wasDismissedWithoutSelection

    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) {
            products
        } else {
            products.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 8.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(
                        if (expanded) RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp) else RoundedCornerShape(8.dp)
                    )
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        expanded = !expanded
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        searchQuery = newValue
                        expanded = true
                    },
                    interactionSource = interactionSource,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.league_spartan_regular)),
                        fontWeight = FontWeight.Medium
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxHeight(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            val isFieldEmpty = searchQuery.isEmpty()

                            if (isFieldEmpty && !isFocused) {
                                val textToShow = if (totalSelectedProducts > 0) displayText else placeholder
                                val color = if (totalSelectedProducts > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary

                                Text(
                                    text = textToShow,
                                    color = color,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.league_spartan_regular)),
                                    fontWeight = if (totalSelectedProducts > 0) FontWeight.Medium else FontWeight.Normal
                                )
                            }

                            if (isFocused || searchQuery.isNotEmpty()) {
                                innerTextField()
                            }
                        }
                    },
                    readOnly = !isFocused && !expanded && totalSelectedProducts > 0,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .focusRequester(focusRequester)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "Toggle dropdown",
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(if (expanded) 180f else 0f)
                        .clickable {
                            expanded = !expanded
                        },
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (expanded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .background(MaterialTheme.colorScheme.secondary)
                        .zIndex(10f),
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (filteredProducts.isEmpty() && searchQuery.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 32.dp),
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
                            Column {
                                filteredProducts.forEach { product ->
                                    val quantity = selectedQuantities[product.id] ?: 0
                                    ProductCard(
                                        product = product,
                                        quantity = quantity,
                                        onQuantityChange = { newQty ->
                                            onQuantityChange(product.id, newQty)
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
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}