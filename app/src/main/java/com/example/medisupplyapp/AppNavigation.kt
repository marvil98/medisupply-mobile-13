package com.example.medisupplyapp

import android.app.Activity
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import androidx.compose.ui.platform.LocalContext
import com.example.medisupplyapp.data.CountryPreferencesRepository
import com.example.medisupplyapp.screen.orders.CreateOrderScreen
import com.example.medisupplyapp.utils.updateLocale
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(userName: String) {
    val navController = rememberNavController()
    var currentLanguage by remember { mutableStateOf("es") }
    val context = LocalContext.current
    val repository = remember { CountryPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("rutas") {
            RutasScreen()
        }

        composable("clientes") {
            ClientesScreen()
        }

        composable("ajustes_regionales") {
            val spanishLabel = context.getString(R.string.language_spanish)
            val englishLabel = context.getString(R.string.language_english)

            RegionalSettingsScreen(
                onLanguageChange = { lang ->
                    currentLanguage = when (lang) {
                        spanishLabel -> "es"
                        englishLabel -> "en"
                        else -> "en"
                    }
                    updateLocale(context, currentLanguage)
                    (context as? Activity)?.recreate()
                },
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "ajustes_regionales",
                onBack = { navController.popBackStack() },
                onCountryChange = { country ->
                    coroutineScope.launch {
                        repository.saveCountry(country)
                    }
                }
            )
        }

        composable("visits") {
            VisitsScreen()
        }

        composable("orders") {
            OrdersScreen()
        }

        composable("orders/create") {
            CreateOrderScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
            )
        }
    }
}

@Composable
fun OrdersScreen() {
    TODO("Not yet implemented")
}

@Composable
fun VisitsScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ClientesScreen() {
    TODO("Not yet implemented")
}

@Composable
fun RutasScreen() {
    TODO("Not yet implemented")
}