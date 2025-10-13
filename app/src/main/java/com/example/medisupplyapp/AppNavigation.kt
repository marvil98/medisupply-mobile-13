package com.example.medisupplyapp

import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import com.example.medisupplyapp.screen.OrdersScreen
import androidx.compose.ui.platform.LocalContext
import com.example.medisupplyapp.screen.FollowOrderScreen
import com.example.medisupplyapp.utils.entities.Order
import com.example.medisupplyapp.utils.entities.OrderStatus
import com.example.medisupplyapp.utils.updateLocale
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
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
            OrdersScreen(
                userName = userName,
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "orders",
            )
        }

        composable("follow_orders") {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.parse("13/10/2025") ?: java.util.Date()
            FollowOrderScreen(
                orders =listOf(
                Order(
                    id= "1",
                    status = OrderStatus.PENDING_APPROVAL,
                    creation_date = date,
                    estimated_release_date =date,
                    last_update = date
                ),
                    Order(
                        id= "2",
                        status = OrderStatus.PROCESSING,
                        creation_date = date,
                        estimated_release_date =date,
                        last_update = date
                    ),
                    Order(
                        id= "3",
                        status = OrderStatus.IN_TRANSIT,
                        creation_date = date,
                        estimated_release_date =date,
                        last_update = date
                    ),
                    Order(
                        id= "4",
                        status = OrderStatus.DELIVERED,
                        creation_date = date,
                        estimated_release_date =date,
                        last_update = date
                    ),
                    Order(
                        id= "5",
                        status = OrderStatus.CANCELLED,
                        creation_date = date,
                        estimated_release_date =date,
                        last_update = date
                    ),Order(
                        id= "6",
                        status = OrderStatus.DELAYED,
                        creation_date = date,
                        estimated_release_date =date,
                        last_update = date
                    ),
            ),
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