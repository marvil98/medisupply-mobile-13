package com.example.medisupplyapp

import androidx.compose.ui.test.*
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class HomeTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    @Test
    fun home_displaysSellerOptions_whenRoleIsSeller() {
        // GIVEN: Iniciamos la UI en modo VENDEDOR
        composeTestRule.setContent {
            HomeContent(
                role = "SELLER",
                userName = "Juan Vendedor",
                userRoleLabel = "Vendedor ID",
                visitsMade = 5,
                totalVisits = 10,
                clientID = 0,
                selectedRoute = "home",
                onNavigate = {},
                onLogout = {}
            )
        }

        // THEN: Verificamos que los elementos del vendedor existen
        // Nota: Usa el texto exacto que definiste en tu stringResource
        val routesTitle = composeTestRule.activity.getString(R.string.routes_title)
        val clientsTitle = composeTestRule.activity.getString(R.string.clients)
        // 1. Verificar tarjeta de Rutas
        composeTestRule.onNodeWithText(routesTitle).assertIsDisplayed()

        // 2. Verificar información de visitas (concatenación)
        composeTestRule.onNodeWithText("5/10", substring = true).assertIsDisplayed()

        // 3. Verificar tarjeta de Clientes
        // Usamos performScrollTo() por si la pantalla es pequeña y el elemento no es visible
        composeTestRule.onNodeWithText(clientsTitle)
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun home_displaysLimitedOptions_whenRoleIsClient() {
        // GIVEN: Iniciamos la UI en modo CLIENTE
        composeTestRule.setContent {
            HomeContent(
                role = "CLIENT",
                userName = "Ana Cliente",
                userRoleLabel = "Cliente ID",
                visitsMade = 0,
                totalVisits = 0,
                clientID = 123,
                selectedRoute = "home",
                onNavigate = {},
                onLogout = {}
            )
        }

        // THEN:
        val routesTitle = composeTestRule.activity.getString(R.string.routes)
        val clientsTitle = composeTestRule.activity.getString(R.string.clients)
        val ordersTitle = composeTestRule.activity.getString(R.string.create_order)
        // 1. Las rutas NO deben existir
        composeTestRule.onNodeWithText(routesTitle).assertDoesNotExist()

        // 2. Los clientes NO deben existir
        composeTestRule.onNodeWithText(clientsTitle).assertDoesNotExist()

        // 3. Los pedidos SÍ deben existir
        composeTestRule.onNodeWithText(ordersTitle).assertIsDisplayed()
    }

    @Test
    fun home_navigatesToClients_whenClicked() {
        var lastRoute = ""

        // GIVEN: UI Vendedor con un callback de navegación capturable
        composeTestRule.setContent {
            HomeContent(
                role = "SELLER",
                userName = "Test",
                userRoleLabel = "Rol",
                visitsMade = 0,
                totalVisits = 0,
                clientID = 0,
                selectedRoute = "home",
                onNavigate = { route -> lastRoute = route }, // Capturamos la ruta
                onLogout = {}
            )
        }

        val clientsTitle = composeTestRule.activity.getString(R.string.clients)

        // WHEN: Hacemos click en Clientes
        composeTestRule.onNodeWithText(clientsTitle)
            .performScrollTo()
            .performClick()

        // THEN: La ruta capturada debe ser "clientes"
        assert(lastRoute == "clientes")
    }

    @Test
    fun home_navigatesToCreateOrderWithId_whenClientClicks() {
        var lastRoute = ""
        val fakeClientId = 999

        // GIVEN: UI Cliente
        composeTestRule.setContent {
            HomeContent(
                role = "CLIENT",
                userName = "Test",
                userRoleLabel = "Rol",
                visitsMade = 0,
                totalVisits = 0,
                clientID = fakeClientId,
                selectedRoute = "home",
                onNavigate = { route -> lastRoute = route },
                onLogout = {}
            )
        }

        val createOrderText = composeTestRule.activity.getString(R.string.create_order) // El texto en R.string.create_order

        // WHEN: Click en Crear Pedido
        composeTestRule.onNodeWithText(createOrderText)
            .performScrollTo()
            .performClick()

        // THEN: La ruta debe contener el ID del cliente (lógica de tu Home.kt original)
        // Nota: revisa tu lógica original, tenías un bug visual en el snippet (dos líneas de string),
        // pero asumiendo que concatena el ID:
        assert(lastRoute == "create_order/$fakeClientId")
    }
}