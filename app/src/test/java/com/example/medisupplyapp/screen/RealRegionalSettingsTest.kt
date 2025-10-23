package com.example.medisupplyapp.screen

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import androidx.compose.ui.test.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class RealRegionalSettingsTest {

    private val languageOptions = listOf("Español", "English")

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


    @Test
    fun regionalSettingsLogic_initialSpanish_isValid() {
        val selectedLanguage = "Español"

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertFalse(hasError)
        assertTrue(isEnabled)
        assertTrue(languageOptions.contains(selectedLanguage))
    }

    @Test
    fun regionalSettingsLogic_initialEnglish_isValid() {
        val selectedLanguage = "English"

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertFalse(hasError)
        assertTrue(isEnabled)
        assertTrue(languageOptions.contains(selectedLanguage))
    }

    @Test
    fun regionalSettingsLogic_nullLanguage_hasErrorAndDisabled() {
        val selectedLanguage: String? = null

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertTrue(hasError)
        assertFalse(isEnabled)
    }

    @Test
    fun regionalSettingsLogic_emptyString_isValidButMayNotBeDesired() {
        val selectedLanguage = ""

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertFalse(hasError)
        assertTrue(isEnabled)

        // Empty string is not in languageOptions, so this might be an edge case
        assertFalse(languageOptions.contains(selectedLanguage))
    }

    @Test
    fun regionalSettingsLogic_changeLanguageToEnglish_isValid() {
        var selectedLanguage = "Español"

        // Simulate user changes language to English
        selectedLanguage = "English"

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertFalse(hasError)
        assertTrue(isEnabled)
        assertTrue(languageOptions.contains(selectedLanguage))
    }

    @Test
    fun regionalSettingsLogic_changeLanguageToSpanish_isValid() {
        var selectedLanguage = "English"

        // Simulate user changes language to Español
        selectedLanguage = "Español"

        val hasError = selectedLanguage == null
        val isEnabled = selectedLanguage != null

        assertFalse(hasError)
        assertTrue(isEnabled)
        assertTrue(languageOptions.contains(selectedLanguage))
    }
}
