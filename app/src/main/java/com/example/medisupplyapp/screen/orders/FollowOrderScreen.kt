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
import com.example.medisupplyapp.components.RoundedButton
import com.example.medisupplyapp.components.SectionCard
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.screen.ErrorScreen
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import kotlin.collections.get


@Composable
fun FollowOrderScreen(
    viewModel: FollowOrdersViewModel = viewModel(),
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
                        onNavigate = onNavigate
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