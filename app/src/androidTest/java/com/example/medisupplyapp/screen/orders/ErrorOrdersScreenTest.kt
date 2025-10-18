package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.medisupplyapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas de UI para el Composable ErrorOrdersScreen.
 * Estas pruebas se ejecutan en un dispositivo Android (carpeta androidTest).
 */
@RunWith(AndroidJUnit4::class)
class ErrorOrdersScreenTest {

    // Regla obligatoria para usar Compose Testing
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun errorScreen_displaysHeaderAndCustomMessage() {
        val testMessage = "Error de conexión: No se pudo cargar la información."
        val padding = PaddingValues(bottom = 56.dp) // Simular el padding del BottomBar

        lateinit var orderHistoryString: String // 1. Variable para almacenar el String

        // 1. Carga el contenido de la UI en la regla de prueba
        composeTestRule.setContent {
            // Obtenemos el contexto (y, por lo tanto, el string) DENTRO del bloque setContent
            orderHistoryString = LocalContext.current.getString(R.string.order_history)

            MaterialTheme {
                ErrorOrdersScreen(
                    paddingValues = padding,
                    message = testMessage
                )
            }
        }

        // 2. Verificación de UI: Encabezado
        // Usamos la variable que ya contiene el string del recurso
        composeTestRule
            .onNodeWithText(
                text = orderHistoryString, // Usamos la variable pre-cargada
                ignoreCase = true
            )
            .assertExists("El encabezado 'Order history' no se encontró.")


        // 3. Verificación de UI: Mensaje de error personalizado
        composeTestRule
            .onNodeWithText(
                text = testMessage,
                ignoreCase = false
            )
            .assertExists("El mensaje de error personalizado no se encontró.")

        // NOTA: Para verificar el centrado de un texto, se requiere una aserción más compleja
        // que verifica las propiedades del Modifier. Para una verificación básica de visibilidad,
        // el código anterior es suficiente.
    }
}
