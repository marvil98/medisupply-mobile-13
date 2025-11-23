package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.OrdersListContent
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme


// En FollowOrderScreen.kt

@Composable
fun FollowOrderScreen(
    viewModel: FollowOrdersViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    // Solo la lógica y recolección de estado se queda aquí
    val uiState = viewModel.ordersState
    val clientId = viewModel.clientIdState

    FollowOrderContent(
        uiState = uiState,
        clientId = clientId,
        selectedRoute = selectedRoute,
        onNavigate = onNavigate,
        onBack = onBack
    )
}

@Composable
fun FollowOrderContent(
    uiState: OrdersUiState,
    clientId: Int?,
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.orders),
                    onBack = onBack
                )
            },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (uiState) {
                    is OrdersUiState.Loading -> {
                        LoadingScreen()
                    }
                    is OrdersUiState.Success -> {
                        OrdersListContent(
                            orders = uiState.orders,
                            paddingValues = PaddingValues(0.dp), // Ya manejado por el box padre o ajusta según necesites
                            onNavigate = onNavigate,
                            clientId = clientId
                        )
                    }
                    is OrdersUiState.Empty -> {
                        ErrorOrdersScreen(
                            paddingValues = PaddingValues(0.dp),
                            message = stringResource(R.string.no_order_history)
                        )
                    }
                    is OrdersUiState.Error -> {
                        ErrorOrdersScreen(
                            paddingValues = PaddingValues(0.dp),
                            message = stringResource(R.string.error_order_history)
                        )
                    }
                }
            }
        }
    }
}