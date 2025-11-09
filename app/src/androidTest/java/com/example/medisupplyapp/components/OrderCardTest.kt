package com.example.medisupplyapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pruebas de UI para el Composable OrderCard.
 * Verifica la correcta visualización de los datos de una orden.
 */
@RunWith(AndroidJUnit4::class)
class OrderCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 1. Crear un objeto Order simulado
    private val mockOrder = Order(
        id = 98765,
        creationDate = Date(1698000000000L), // Oct 22, 2023
        estimatedReleaseDate = Date(1700000000000L), // Nov 14, 2023
        lastUpdate = Date(1705000000000L), // Jan 12, 2024
        status = OrderStatus.IN_TRANSIT // Estado "En camino"
    )

    // Formato de fecha usado en OrderCard: dd/MM/yyyy
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    @Test
    fun orderCard_displaysAllOrderDetails() {
        composeTestRule.setContent {
            MaterialTheme {
                OrderCard(order = mockOrder)
            }
        }

        // 2. Verificación del ID de la Orden
        // El texto debería contener el string de recurso y el ID
        val expectedOrderIdText = "Pedido #98765" // Asumiendo que R.string.order es "Pedido"
        composeTestRule
            .onNodeWithText(expectedOrderIdText, ignoreCase = false)
            .assertExists("El ID de la orden no se muestra correctamente.")

        // 3. Verificación de la Última Actualización
        val lastUpdateFormatted = dateFormatter.format(mockOrder.lastUpdate)
        val expectedLastUpdateText = "Última actualización: $lastUpdateFormatted" // Asumiendo R.string.last_update

        composeTestRule
            .onNodeWithText(expectedLastUpdateText, ignoreCase = false)
            .assertExists("La fecha de última actualización no se muestra correctamente.")

        // 4. Verificación del Estado (DisplayName)
        // El texto del estado debe coincidir con el displayName de OrderStatus.IN_TRANSIT
        // NOTA: Para que esta prueba sea robusta, necesitas usar el string del recurso R.string.in_transit.
        // Asumiendo que OrderStatus.IN_TRANSIT.displayName es un ID de recurso que se resuelve a "En camino".
        // Si el string no se resuelve, esta prueba podría fallar.
        composeTestRule
            .onNodeWithText("En camino", ignoreCase = true) // Usando el texto literal para la prueba
            .assertExists("El estado 'En camino' no es visible.")
    }
}
