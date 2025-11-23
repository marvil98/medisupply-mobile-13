package com.example.medisupplyapp.screen.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.medisupplyapp.R
import org.junit.Rule
import org.junit.Test

class SplashTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    // ==========================================
    // PRUEBAS DE LA PANTALLA DE CARGA (Loading)
    // ==========================================
    @Test
    fun splashLoading_displaysLogoAndAppName() {
        // GIVEN: Cargamos solo la UI visual (sin lógica de espera)
        composeTestRule.setContent {
            SplashLoadingContent()
        }

        // THEN: Verificamos los elementos

        // 1. Logo (buscamos por content description)
        composeTestRule.onNodeWithContentDescription("MediSupply Logo")
            .assertIsDisplayed()

        // 2. Texto "Medi"
        composeTestRule.onNodeWithText("Medi")
            .assertIsDisplayed()

        // 3. Texto "Supply"
        composeTestRule.onNodeWithText("Supply")
            .assertIsDisplayed()
    }

    // ==========================================
    // PRUEBAS DE LA PANTALLA DE INICIO (Login Button)
    // ==========================================
    @Test
    fun splashScreen_displaysLoginButtonAndLogo() {
        // GIVEN: Pantalla con botón
        composeTestRule.setContent {
            SplashScreen(onNavigateToLogin = {})
        }

        // THEN:
        // 1. Logo
        composeTestRule.onNodeWithContentDescription("MediSupply Logo")
            .assertIsDisplayed()

        // 2. Botón de Iniciar Sesión (usando el recurso de string)
        val loginText = composeTestRule.activity.getString(R.string.login)

        composeTestRule.onNodeWithText(loginText)
            .assertIsDisplayed()
            .assertHasClickAction() // Verificamos que sea clickeable
    }

    @Test
    fun splashScreen_navigates_whenLoginClicked() {
        var navigated = false

        // GIVEN: Pantalla con callback de navegación
        composeTestRule.setContent {
            SplashScreen(
                onNavigateToLogin = { navigated = true }
            )
        }

        val loginText = composeTestRule.activity.getString(R.string.login)

        // WHEN: Hacemos click en el botón
        composeTestRule.onNodeWithText(loginText)
            .performClick()

        // THEN: La variable debe haber cambiado a true
        assert(navigated)
    }
}