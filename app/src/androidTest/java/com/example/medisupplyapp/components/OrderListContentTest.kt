package com.example.medisupplyapp.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class OrdersListContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Datos simulados para la lista
    private val mockOrders = listOf(
        Order(id = 100, creationDate = Date(), estimatedReleaseDate = Date(), lastUpdate = Date(), status = OrderStatus.DELIVERED),
        Order(id = 200, creationDate = Date(), estimatedReleaseDate = Date(), lastUpdate = Date(), status = OrderStatus.PROCESSING),
        Order(id = 300, creationDate = Date(), estimatedReleaseDate = null, lastUpdate = Date(), status = OrderStatus.CANCELLED),
    )

    @Test
    fun ordersListContent_displaysStaticElementsAndOrderCards() {
        composeTestRule.setContent {
            MaterialTheme {
                OrdersListContent(
                    orders = mockOrders,
                    paddingValues = PaddingValues(0.dp), // Padding mínimo para simplificar
                    onNavigate = {}
                )
            }
        }

        // 1. Verificación del botón "Seguimiento de pedidos" (R.string.follow_order)
        // Asumiendo que R.string.follow_order es "Seguimiento de pedidos"
        composeTestRule
            .onNodeWithText("Seguimiento de pedidos", ignoreCase = true)
            .assertExists("El botón 'Seguimiento de pedidos' no se encontró.")

        // 2. Verificación del encabezado "Historial de pedidos" (R.string.order_history)
        // Asumiendo que R.string.order_history es "Historial de pedidos"
        composeTestRule
            .onNodeWithText("Historial de pedidos", ignoreCase = true)
            .assertExists("El encabezado 'Historial de pedidos' no se encontró.")

        // 3. Verificación de todas las tarjetas de pedidos
        // Verificamos que se muestren los IDs de todas las órdenes.
        mockOrders.forEach { order ->
            val expectedIdText = "Pedido #${order.id}" // Basado en el formato de OrderCard
            composeTestRule
                .onNodeWithText(expectedIdText, ignoreCase = false)
                .assertExists("La tarjeta para el pedido #${order.id} no se encontró.")
        }
    }
}
