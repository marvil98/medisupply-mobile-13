package com.example.medisupplyapp

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import androidx.compose.ui.platform.LocalContext
import com.example.medisupplyapp.utils.updateLocale

@Composable
fun AppNavigation(userName: String) {
    val navController = rememberNavController()
    var currentLanguage by remember { mutableStateOf("es") }
    val context = LocalContext.current

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
            RegionalSettingsScreen(
                currentLanguage = if (currentLanguage == "es") "Español" else "English",
                onLanguageChange = { lang ->
                    currentLanguage = if (lang == "Español") "es" else "en"
                    updateLocale(context, currentLanguage)
                },
                onSave = { navController.popBackStack() },
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "ajustes_regionales",
                onBack = { navController.popBackStack() }
            )
        }

        composable("visits") {
            VisitsScreen()
        }

        composable("orders") {
            OrdersScreen()
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