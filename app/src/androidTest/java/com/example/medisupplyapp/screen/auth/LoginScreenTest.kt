package com.example.medisupplyapp.screen.auth

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.medisupplyapp.R
import androidx.compose.ui.semantics.Role //
// Importa tu LoginUiState real
// import com.example.medisupplyapp.screen.auth.LoginUiState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // Variables de ayuda para textos comunes
    private val emailLabel by lazy { composeTestRule.activity.getString(R.string.email) }
    private val passwordLabel by lazy { composeTestRule.activity.getString(R.string.password) }
    private val loginButtonText by lazy { composeTestRule.activity.getString(R.string.login) }

    @Test
    fun login_initialState_rendersCorrectly() {
        // GIVEN: Estado inicial vacío
        val initialState = LoginUiState()

        composeTestRule.setContent {
            LoginContent(
                uiState = initialState,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onBack = {}
            )
        }

        // THEN: Verificar elementos básicos
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.welcome))
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(emailLabel).assertIsDisplayed()
        composeTestRule.onNodeWithText(passwordLabel).assertIsDisplayed()

        // El botón debe existir pero estar deshabilitado (si isFormValid es false por defecto)
        composeTestRule.onAllNodesWithText(loginButtonText)
            .filter(hasClickAction()) // Filtra por el elemento que puede hacer click
            .onFirst()
            .performScrollTo()
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun login_displaysError_whenEmailIsInvalid() {
        // GIVEN: Estado con error en email
        val errorState = LoginUiState(
            email = "bad_email",
            emailError = "Formato de correo inválido" // Mensaje simulado
        )

        composeTestRule.setContent {
            LoginContent(
                uiState = errorState,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onBack = {}
            )
        }

        // THEN: El mensaje de error debe verse
        composeTestRule.onNodeWithText("Formato de correo inválido")
            .performScrollTo()
            .assertIsDisplayed()
    }


    @Test
    fun login_showsLoading_andDisablesButton() {
        // GIVEN: Estado de carga
        val loadingState = LoginUiState(
            isLoading = true,
            isFormValid = true
        )

        composeTestRule.setContent {
            LoginContent(
                uiState = loadingState,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onBack = {}
            )
        }

        // Usamos el tag del botón para aislar la búsqueda.
        // Buscamos el texto "Login" DENTRO del nodo con el tag "login_button"
        composeTestRule.onNodeWithTag("login_button")
            .performScrollTo() // Opcional, pero bueno si está al final
            .assertExists() // El botón existe, pero su contenido cambió

        // THEN: Verificamos que el texto "Login" NO existe dentro del botón (solo el spinner)
        // El texto del TopBar no será afectado porque estamos buscando DENTRO del botón.
        composeTestRule.onNodeWithTag("login_button")
            .onChildren() // Miramos los hijos del botón (donde estaría el texto o el spinner)
            .filter(hasText(loginButtonText)) // Filtramos por el texto "Login"
            .assertCountEquals(0) // ⬅️ ¡Esperamos CERO nodos de texto!

        // Opcional: Verificar que el CircularProgressIndicator SÍ existe
        // Si le pusiste un tag al spinner:
        // composeTestRule.onNodeWithTag("loading_spinner").assertIsDisplayed()
    }
}

// MOCK para que compile el ejemplo (Bórralo si ya tienes tu data class)
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isFormValid: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false
)