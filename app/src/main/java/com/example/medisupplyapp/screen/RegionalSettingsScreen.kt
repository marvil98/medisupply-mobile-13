package com.example.medisupplyapp.screen

import android.content.pm.PackageManager
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
import java.text.SimpleDateFormat
import java.util.Date
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
fun getAppVersionInfo(): Pair<String, String> {
    val context = LocalContext.current
    val packageName = context.packageName

    // Simulación de la fecha de la última compilación o versión
    // En una app real, esta fecha vendría de BuildConfig (generada en Gradle)
    val compileDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    return try {
        val packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        // Usamos el nombre de la versión y el código de versión (longVersionCode para API 28+)
        val versionName = packageInfo.versionName
        val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            packageInfo.longVersionCode.toString()
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toString()
        }

        Pair("v$versionName (build $versionCode)", compileDate)
    } catch (e: PackageManager.NameNotFoundException) {
        Pair(stringResource(R.string.visits), compileDate)
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
    val (versionText, dateText) = getAppVersionInfo()
    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.regional_settings),
                    onBack = onBack
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally // Para centrar los Text
                ) {
                    // --- SECCIÓN DE VERSIÓN Y FECHA ---
                    Spacer(modifier = Modifier.height(8.dp)) // Pequeño espacio superior

                    Text(
                        text = stringResource(R.string.version_label, versionText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.date_label, dateText),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Espacio entre la versión y la navegación
                    Spacer(modifier = Modifier.height(8.dp))

                    // --- NAVEGACIÓN PRINCIPAL ---
                    FooterNavigation(selectedRoute, onNavigate)
                }
                        },
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
