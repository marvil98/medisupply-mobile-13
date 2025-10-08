package com.example.medisupplyapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.components.Header
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun Home(userName: String, selectedRoute: String, onNavigate: (String) -> Unit) {
    MediSupplyTheme {
        Scaffold(
            topBar = { Header(userName) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.activity_prompt),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}