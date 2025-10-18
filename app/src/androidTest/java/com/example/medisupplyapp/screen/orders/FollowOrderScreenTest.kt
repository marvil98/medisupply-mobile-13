package com.example.medisupplyapp.screen.orders

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

/**
 * Pruebas de UI para el Composable FollowOrderScreen, enfocadas en los estados del ViewModel.
 */
@RunWith(AndroidJUnit4::class)
class FollowOrderScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // 1. Función Mock para simular el ViewModel en el estado de Éxito/Datos
    private fun setupSuccessViewModel(): FollowOrdersViewModel {
        val mockViewModel = mock(FollowOrdersViewModel::class.java)
        val mockOrders = listOf(
            Order(id = "1", creationDate = mock(), estimatedReleaseDate = mock(), lastUpdate = mock(), status = mock()),
        )
        // Usamos MutableStateFlow para simular el LiveData/StateFlow del ViewModel
        `when`(mockViewModel.ordersState).thenReturn(OrdersUiState.Success(mockOrders))
        return mockViewModel
    }

    // 2. Función Mock para simular el ViewModel en el estado Vacío
    private fun setupEmptyViewModel(): FollowOrdersViewModel {
        val mockViewModel = mock(FollowOrdersViewModel::class.java)
        `when`(mockViewModel.ordersState).thenReturn(OrdersUiState.Empty)
        return mockViewModel
    }


    @Test
    fun followOrderScreen_showsLoadingState() {
        // Configuramos un ViewModel Mock para retornar el estado de Carga
        val mockViewModel = mock(FollowOrdersViewModel::class.java)
        `when`(mockViewModel.ordersState).thenReturn(OrdersUiState.Loading)

        composeTestRule.setContent {
            MaterialTheme {
                FollowOrderScreen(
                    viewModel = mockViewModel,
                    onNavigate = {},
                    selectedRoute = "orders",
                    onBack = {}
                )
            }
        }

        // Verifica que se muestre la pantalla de carga
        // Esto depende de cómo LoadingScreen implemente su UI (ej: un texto)
        composeTestRule.onNodeWithText("Cargando...").assertExists()
    }

    @Test
    fun followOrderScreen_showsEmptyState() {
        val mockViewModel = setupEmptyViewModel()

        composeTestRule.setContent {
            MaterialTheme {
                FollowOrderScreen(
                    viewModel = mockViewModel,
                    onNavigate = {},
                    selectedRoute = "orders",
                    onBack = {}
                )
            }
        }

        // Verifica que se muestre el texto de la pantalla de error (sin pedidos)
        composeTestRule.onNodeWithText(
            text = composeTestRule.activity.getString(R.string.no_order_history),
            ignoreCase = true
        ).assertExists("No se mostró la pantalla de estado Empty (Sin pedidos).")
    }

    // Puedes agregar más pruebas para los estados Success y Error...
}
