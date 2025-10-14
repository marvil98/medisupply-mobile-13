package com.example.medisupplyapp

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import com.example.medisupplyapp.screen.orders.OrdersHomeScreen
import androidx.compose.ui.platform.LocalContext
import com.example.medisupplyapp.screen.orders.FollowOrderScreen
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus
import com.example.medisupplyapp.utils.updateLocale
import java.text.SimpleDateFormat
import java.util.Locale

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
            OrdersHomeScreen(
                userName = userName,
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "orders",
            )
        }

        composable("follow_orders") {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse("13/10/2025") ?: java.util.Date()
            FollowOrderScreen(
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "orders",
                onBack = { navController.popBackStack() }
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