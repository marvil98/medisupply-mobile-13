package com.example.medisupplyapp.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionalSettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLanguageSelection_andSaveButtonEnabled() {
        var selectedLanguage = "Español"

        composeTestRule.setContent {
            MediSupplyTheme {
                RegionalSettingsScreen(
                    currentLanguage = selectedLanguage,
                    onLanguageChange = { selectedLanguage = it },
                    onNavigate = {},
                    selectedRoute = "home",
                    onBack = {},
                    onSave = {}
                )
            }
        }

        // Verifica que el texto "Idioma" esté visible
        composeTestRule.onNodeWithText("Idioma").assertIsDisplayed()

        // Simula selección de idioma
        composeTestRule.onNodeWithText("Idioma").performClick()
        composeTestRule.onNodeWithText("English").performClick()

        // Verifica que el idioma seleccionado cambió
        assert(selectedLanguage == "English")

        // Verifica que el botón guardar esté habilitado
        composeTestRule.onNodeWithText("Guardar").assertIsEnabled()
    }
}
