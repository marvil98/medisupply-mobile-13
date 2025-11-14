package com.example.medisupplyapp

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.components.SectionCard
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.components.Header
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun Home( selectedRoute: String, onNavigate: (String) -> Unit) {
    val viewModel: HomeViewModel = viewModel()

    val dailyRoute by viewModel.dailyRoute.collectAsState()
    val visitsMade by viewModel.visitsMade.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val role by viewModel.role.collectAsState()
    val clientID by viewModel.clientID.collectAsState()

    MediSupplyTheme {
        Scaffold(
            topBar = { Header(userName, onNavigate) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
                containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.activity_prompt),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(28.dp))
                if (role == "SELLER") {
                    SectionCard(
                        title = stringResource(R.string.routes_title),
                        subtitle = "${visitsMade}/${dailyRoute.numberVisits} ${stringResource(R.string.routes_subtitle)}",
                        centered = true,
                        onClick = { onNavigate("routes") }
                    )

                    SectionCard(
                        title = stringResource(R.string.visits),
                        options = listOf(Pair(stringResource(R.string.register_visit), "register_visit"),
                            Pair(stringResource(R.string.suggest_product),"suggest_product" )),
                        onOptionClick = { onNavigate(it) }
                    )
                    SectionCard(
                        title = stringResource(R.string.orders),
                        options = listOf(Pair(stringResource(R.string.create_order), "create_order"),
                            Pair(stringResource(R.string.follow_order), "follow_orders")),
                        onOptionClick = { onNavigate(it) }
                    )

                    SectionCard(
                        title = stringResource(R.string.clients),
                        centered = true,
                        onClick = { onNavigate("clientes") }
                    )
                }
                if (role == "CLIENT") {
                    SectionCard(
                        title = stringResource(R.string.orders),
                        options = listOf(Pair(stringResource(R.string.create_order), "create_order"),
                            Pair(stringResource(R.string.follow_order), "follow_orders")),
                        onOptionClick = { route ->
                            val finalRoute = if (route == "create_order") {
                                "create_order/${clientID}"
                                "create_order/${clientID}"
                            } else {
                                route
                            }
                            onNavigate(finalRoute)
                        }
                    )
                }
            }
        }
    }
}