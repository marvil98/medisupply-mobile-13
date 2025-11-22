package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.OrdersListContent
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme


@Composable
fun FollowOrderScreen(
    viewModel: FollowOrdersViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    val uiState = viewModel.ordersState
    val clientId = viewModel.clientIdState

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
            when (uiState) {
                is OrdersUiState.Loading -> {
                    LoadingScreen()
                }
                is OrdersUiState.Success -> {
                    OrdersListContent(
                        orders = uiState.orders,
                        paddingValues = paddingValues,
                        onNavigate = onNavigate,
                        clientId = clientId
                    )
                }
                is OrdersUiState.Empty -> {
                    ErrorOrdersScreen(
                        paddingValues = paddingValues,
                        stringResource(R.string.no_order_history)
                    )
                }
                is OrdersUiState.Error -> {
                    ErrorOrdersScreen(
                        paddingValues = paddingValues,
                        stringResource(R.string.error_order_history)
                    )
                }
            }
        }
    }
}