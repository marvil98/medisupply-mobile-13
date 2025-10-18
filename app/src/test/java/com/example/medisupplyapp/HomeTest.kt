package com.example.medisupplyapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun home_displaysUserName() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }

    @Test
    fun home_displaysAllSectionCards() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Assert - All section cards should be displayed
        composeTestRule.onNodeWithText("Rutas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Visitas").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pedidos").assertIsDisplayed()
        composeTestRule.onNodeWithText("Clientes").assertIsDisplayed()
    }

    @Test
    fun home_routesCardNavigatesToRutas() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Click on routes card
        composeTestRule.onNodeWithText("Rutas").performClick()

        // Assert
        assert(navigatedRoute == "rutas")
    }

    @Test
    fun home_clientsCardNavigatesToClientes() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Click on clients card
        composeTestRule.onNodeWithText("Clientes").performClick()

        // Assert
        assert(navigatedRoute == "clientes")
    }

    @Test
    fun home_visitsCardShowsOptions() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Assert - Visit options should be displayed
        composeTestRule.onNodeWithText("Registrar Visita").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sugerir Producto").assertIsDisplayed()
    }

    @Test
    fun home_ordersCardShowsOptions() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Assert - Order options should be displayed
        composeTestRule.onNodeWithText("Crear Pedido").assertIsDisplayed()
        composeTestRule.onNodeWithText("Seguir Pedido").assertIsDisplayed()
    }

    @Test
    fun home_visitOptionClickNavigatesCorrectly() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Click on register visit option
        composeTestRule.onNodeWithText("Registrar Visita").performClick()

        // Assert
        assert(navigatedRoute == "Registrar Visita")
    }

    @Test
    fun home_orderOptionClickNavigatesCorrectly() {
        // Arrange
        val userName = "Test User"
        var navigatedRoute = ""

        // Act
        composeTestRule.setContent {
            Home(
                userName = userName,
                selectedRoute = "home",
                onNavigate = { navigatedRoute = it }
            )
        }

        // Click on create order option
        composeTestRule.onNodeWithText("Crear Pedido").performClick()

        // Assert
        assert(navigatedRoute == "Crear Pedido")
    }
}
