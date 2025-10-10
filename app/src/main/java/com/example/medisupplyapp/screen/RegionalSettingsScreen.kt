package com.example.medisupplyapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.CustomDropdown
import com.example.medisupplyapp.components.SimpleTopBar
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun RegionalSettingsScreen(
    currentLanguage: String,
    onLanguageChange: (String) -> Unit,
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var selectedLanguage by remember { mutableStateOf(currentLanguage) }
    val languageOptions = listOf("Español", "English")
    val hasError = selectedLanguage == null

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                title = stringResource(R.string.regional_settings),
                onBack = onBack
            ) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
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
                CustomDropdown(
                    label = "Idioma",
                    options = languageOptions,
                    selected = selectedLanguage,
                    onSelect = {
                        selectedLanguage = it
                        onLanguageChange(it)
                    },
                    hasError = hasError
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onSave,
                    enabled = selectedLanguage != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}
