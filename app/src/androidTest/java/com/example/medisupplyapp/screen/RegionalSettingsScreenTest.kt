package com.example.medisupplyapp.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RegionalSettingsScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para las funciones callback que recibe el composable
    private val onLanguageChange: (String) -> Unit = mock()
    private val onNavigate: (String) -> Unit = mock()
    private val onBack: () -> Unit = mock()
    private val onSave: () -> Unit = mock()

    @Test
    fun dropdown_selection_updatesSelectedLanguage_andEnablesSaveButton() {
        // Montar el composable con el estado inicial y los mocks
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = "Español",
                onLanguageChange = onLanguageChange,
                onNavigate = onNavigate,
                selectedRoute = "settings",
                onBack = onBack,
                onSave = onSave
            )
        }

        // Simular click para abrir el dropdown (buscando el label "Idioma")
        composeTestRule.onNodeWithText("Idioma").performClick()

        // Simular selección de "English"
        composeTestRule.onNodeWithText("English").performClick()

        // Comprobar que el dropdown muestra la opción seleccionada "English"
        composeTestRule.onNodeWithText("English").assertExists()

        // Comprobar que el botón "Save" está habilitado
        composeTestRule.onNodeWithText("Save").assertIsEnabled()

        // Verificar que se llamó el callback onLanguageChange con el valor "English"
        verify(onLanguageChange).invoke("English")
    }
}
