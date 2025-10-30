package com.example.medisupplyapp.screen.visits

import com.example.medisupplyapp.screen.orders.ErrorOrdersScreen
import com.example.medisupplyapp.screen.orders.FollowOrdersViewModel
import com.example.medisupplyapp.screen.orders.OrdersUiState


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
import androidx.compose.runtime.collectAsState


@Composable
fun DailyRouteScreen(
    viewModel: DailyRouteVireModel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    val numberVisits = viewModel.dailyRoute.collectAsState().value.numberVisits
    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.routes),
                    onBack = onBack
                ) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            if (numberVisits > 0 ){
                // pintr mapa
                NoDailyRouteScreen(
                    paddingValues = paddingValues,
                    stringResource(R.string.no_daily_routes)
                )
            } else {
                NoDailyRouteScreen(
                    paddingValues = paddingValues,
                    stringResource(R.string.no_daily_routes)
                )

            }

        }
    }
}