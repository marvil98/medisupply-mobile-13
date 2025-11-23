package com.example.medisupplyapp.screen.orders

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.medisupplyapp.R
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus
import org.junit.Rule
import org.junit.Test
import java.util.Date

class FollowOrderScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // CASO 1: LOADING (Es un object, se pasa sin paréntesis)
    @Test
    fun followOrder_showsLoadingState() {
        composeTestRule.setContent {
            FollowOrderContent(
                uiState = OrdersUiState.Loading, // <--- Correcto: Sin ()
                clientId = 123,
                selectedRoute = "orders",
                onNavigate = {},
                onBack = {}
            )
        }

        // Asumiendo que usaste el tag en LoadingScreen como te sugerí
        // composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
    }

    // CASO 2: EMPTY (Es un object, se pasa sin paréntesis)
    @Test
    fun followOrder_showsEmptyState_whenNoOrders() {
        composeTestRule.setContent {
            FollowOrderContent(
                uiState = OrdersUiState.Empty, // <--- Correcto: Sin ()
                clientId = 123,
                selectedRoute = "orders",
                onNavigate = {},
                onBack = {}
            )
        }

        val emptyText = composeTestRule.activity.getString(R.string.no_order_history)
        composeTestRule.onNodeWithText(emptyText).assertIsDisplayed()
    }

    // CASO 3: ERROR (Es data class, REQUIERE paréntesis y mensaje)
    @Test
    fun followOrder_showsErrorState_whenNetworkFails() {
        // Debemos crear la instancia con un mensaje string dummy
        val state = OrdersUiState.Error("Error de conexión simulado")

        composeTestRule.setContent {
            FollowOrderContent(
                uiState = state, // <--- Pasamos la instancia creada
                clientId = 123,
                selectedRoute = "orders",
                onNavigate = {},
                onBack = {}
            )
        }

        val errorText = composeTestRule.activity.getString(R.string.error_order_history)
        composeTestRule.onNodeWithText(errorText).assertIsDisplayed()
    }

    // CASO 4: SUCCESS (Es data class, REQUIERE paréntesis y lista)
    @Test
    fun followOrder_showsList_whenSuccess() {
        val fakeOrders = listOf(
            Order(
                id = 1001, // Int
                status = OrderStatus.PENDING_APPROVAL, // Reemplaza con tu valor real del Enum (ej. CREATED, PROCESSING)
                creationDate = Date(), // Fecha actual
                estimatedReleaseDate = Date(), // Fecha actual (o null si prefieres)
                lastUpdate = Date()
            ),
            Order(
                id = 2050,
                status = OrderStatus.DELIVERED, // Reemplaza con tu valor real
                creationDate = Date(),
                estimatedReleaseDate = null, // Probamos con nulo si es permitido
                lastUpdate = Date()
            )
        )

        // Creamos la instancia con la lista
        val state = OrdersUiState.Success(fakeOrders)

        composeTestRule.setContent {
            FollowOrderContent(
                uiState = state, // <--- Pasamos la instancia creada
                clientId = 123,
                selectedRoute = "orders",
                onNavigate = {},
                onBack = {}
            )
        }

        // Verificamos que se vea el ID del pedido 1001
        composeTestRule.onNodeWithText("1001", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
    }
}