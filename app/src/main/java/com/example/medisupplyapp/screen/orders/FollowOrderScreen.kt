package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.OrderCard
import com.example.medisupplyapp.components.OrdersListContent
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.Order
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import kotlin.collections.get


@Composable
fun FollowOrderScreen(
    viewModel: FollowOrdersViewModel = viewModel(),
    onOrderClick: (String) -> Unit = {},
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    val uiState = viewModel.ordersState
    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.orders),
                    onBack = onBack
                ) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            when (uiState) {
                is OrdersUiState.Loading -> {
                    LoadingScreen()
                }

                is OrdersUiState.Success -> {
                    OrdersListContent(
                        orders = uiState.orders,
                        paddingValues = paddingValues,
                        onOrderClick = onOrderClick
                    )
                }

                is OrdersUiState.Empty -> {
                    EmptyOrdersScreen(paddingValues = paddingValues)
                }

                is OrdersUiState.Error -> {
                    ErrorScreen(
                        paddingValues = paddingValues,
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyOrdersScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.order_history),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        // Icono de caja
        Icon(
            painter = painterResource(id = R.drawable.ic_box),
            contentDescription = stringResource(R.string.orders)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¡Ups! Aún no tienes pedidos registrados.",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background( MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.order_history),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            // Icono de caja
            Icon(
                painter = painterResource(id = R.drawable.ic_box),
                contentDescription = stringResource(R.string.orders)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Ups! No pudimos obtener los pedidos. Intenta nuevamente.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}
