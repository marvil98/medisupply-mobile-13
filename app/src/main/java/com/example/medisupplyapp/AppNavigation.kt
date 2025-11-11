package com.example.medisupplyapp

import android.app.Activity
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.medisupplyapp.screen.RegionalSettingsScreen
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.medisupplyapp.data.CountryPreferencesRepository
import com.example.medisupplyapp.screen.auth.LoginScreen
import com.example.medisupplyapp.screen.auth.SplashScreen
import com.example.medisupplyapp.screen.auth.SplashScreenWithAutoNavigation
import com.example.medisupplyapp.screen.orders.CreateOrderClientScreen
import com.example.medisupplyapp.screen.orders.CreateOrderScreen
import com.example.medisupplyapp.utils.updateLocale
import kotlinx.coroutines.launch
import com.example.medisupplyapp.screen.orders.FollowOrderScreen
import com.example.medisupplyapp.screen.orders.OrderDetailScreen
import com.example.medisupplyapp.screen.visits.DailyRouteScreen
import com.example.medisupplyapp.screen.visits.RegisterEvidenceScreen
import com.example.medisupplyapp.screen.visits.RegisterVisitScreen

fun mapCountryToCode(countryName: String): String {
    return when (countryName) {
        "Colombia" -> "CO"
        "Perú" -> "PE"
        "Ecuador" -> "EC"
        "México" -> "MX"
        else -> "CO"
    }
}

@Composable
fun AppNavigation(userName: String) {
    val navController = rememberNavController()
    var currentLanguage by remember { mutableStateOf("es") }
    val context = LocalContext.current
    val repository = remember { CountryPreferencesRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    val selectedCountry by repository.selectedCountry.collectAsState(initial = "Colombia")
    val regionalCountryCode = mapCountryToCode(selectedCountry)

    NavHost(
        navController = navController,
        startDestination = "splash_auto"
    ) {
        composable("splash_auto") {
            SplashScreenWithAutoNavigation(
                onNavigateToSplash = {
                    navController.navigate("splash") {
                        popUpTo("splash_auto") { inclusive = true }
                    }
                }
            )
        }

        composable("splash") {
            SplashScreen (
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("routes") {
            DailyRouteScreen (
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "routes",
                onBack = { navController.popBackStack() }
            )
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
            RegisterVisitScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
            )
        }

        composable("register_visit") {
            RegisterVisitScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
            )
        }

        composable("orders") {
            FollowOrderScreen(
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "orders",
                onBack = { navController.popBackStack() }
            )
        }

        composable("follow_orders") {
            FollowOrderScreen(
                onNavigate = { route -> navController.navigate(route) },
                selectedRoute = "orders",
                onBack = { navController.popBackStack() }
            )
        }

        composable("create_order") {
            CreateOrderScreen(
                    onNavigate = { route -> navController.navigate(route) },
                    onBack = { navController.popBackStack() },
                    onNavigateDetail = { route -> navController.navigate("home") },
            )
        }

        composable(
            route = "evidencias/{visitId}?clientId={clientId}",
            arguments = listOf(
                navArgument("visitId") { type = NavType.IntType },
                navArgument("clientId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val visitId = backStackEntry.arguments?.getInt("visitId") ?: -1
            val clientId = backStackEntry.arguments?.getInt("clientId") ?: -1

            RegisterEvidenceScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
                selectedRoute = "visits",
                visitId = visitId,
                clientId = clientId,
                regionalCode = regionalCountryCode
            )
        }

        composable("create_order/{clientId}") {
            CreateOrderClientScreen(
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
                onNavigateDetail = { route -> navController.navigate("home") },
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "order_detail/{orderId}",
            arguments = listOf(navArgument("orderId") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            OrderDetailScreen(
                orderId = orderId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun ClientesScreen() {
    TODO("Not yet implemented")
}

@Composable
fun RutasScreen() {
    TODO("Not yet implemented")
}
