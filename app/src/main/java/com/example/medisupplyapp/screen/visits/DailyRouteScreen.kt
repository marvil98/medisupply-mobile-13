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
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun DailyRouteScreen(
    viewModel: DailyRouteVireModel = viewModel(),
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
) {
    val numberVisits = viewModel.dailyRoute.collectAsState().value.numberVisits
    val visitPoints = viewModel.dailyRoute.collectAsState().value.visits

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
            if (numberVisits > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    MapWithPoints(visitPoints = visitPoints)
                }
            } else {
                NoDailyRouteScreen(
                    paddingValues = paddingValues,
                    stringResource(R.string.no_daily_routes)
                )
            }
        }
    }
}



