package com.example.medisupplyapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.CustomDropdown
import com.example.medisupplyapp.components.SimpleTopBar
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.util.Locale

@Composable
fun getDefaultLanguage(): String {
    val context = LocalContext.current
    return when (Locale.getDefault().language) {
        "es" -> context.getString(R.string.language_spanish)
        "en" -> context.getString(R.string.language_english)
        else -> context.getString(R.string.language_english)
    }
}

@Composable
fun RegionalSettingsScreen(
    onLanguageChange: (String) -> Unit,
    onNavigate: (String) -> Unit,
    selectedRoute: String,
    onBack: () -> Unit,
    onCountryChange: (String) -> Unit,
) {
    val defaultLanguage = getDefaultLanguage()
    var selectedLanguage by remember { mutableStateOf(defaultLanguage) }
    val languageLabel = stringResource(R.string.language_label)
    val languageOptions = listOf(
        stringResource(R.string.language_spanish),
        stringResource(R.string.language_english)
    )

    val countryLabel = stringResource(R.string.country_label)
    val countryOptions = listOf(
        stringResource(R.string.country_colombia),
        stringResource(R.string.country_peru),
        stringResource(R.string.country_ecuador),
        stringResource(R.string.country_mexico)
    )

    var selectedCountry by remember { mutableStateOf(countryOptions.first()) }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.regional_settings),
                    onBack = onBack
                )
            },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
        ) { innerPadding ->
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomDropdown(
                        label = languageLabel,
                        options = languageOptions,
                        selected = selectedLanguage,
                        onSelect = { selectedLanguage = it },
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomDropdown(
                        label = countryLabel,
                        options = countryOptions,
                        selected = selectedCountry,
                        onSelect = { selectedCountry = it }
                    )
                    Spacer(modifier = Modifier.height(94.dp))
                    Button(
                        onClick = {
                            onLanguageChange(selectedLanguage)
                            onCountryChange(selectedCountry)
                        },
                        modifier = Modifier
                            .height(48.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            }
        }
    }
}
