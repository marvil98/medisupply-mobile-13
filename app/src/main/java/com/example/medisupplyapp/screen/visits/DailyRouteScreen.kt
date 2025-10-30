package com.example.medisupplyapp.screen.visits

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.MapWithPoints
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun DailyRouteScreen(
    viewModel: DailyRouteViewModel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.routes),
                    onBack = onBack
                )
            },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->

            when (val state = uiState) {
                is DailyRouteUiState.Loading -> {
                    LoadingScreen()
                }

                is DailyRouteUiState.Success -> {
                    if (state.dailyRoute.numberVisits > 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            MapWithPoints(visitPoints = state.dailyRoute.visits)
                        }
                    } else {
                        NoDailyRouteScreen(
                            paddingValues = paddingValues,
                            stringResource(R.string.no_daily_routes)
                        )
                    }
                }

                is DailyRouteUiState.Error -> {
                    NoDailyRouteScreen(
                        paddingValues = paddingValues,
                        stringResource(R.string.no_daily_routes)
                    )
                }
            }
        }
    }
}