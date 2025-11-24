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


}