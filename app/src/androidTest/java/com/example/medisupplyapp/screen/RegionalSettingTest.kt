package com.example.medisupplyapp.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.medisupplyapp.MainActivity
import org.junit.Rule
import org.junit.Test

@get:Rule
val composeTestRule = createAndroidComposeRule<MainActivity>()

@Test
fun regionalSettingsScreen_displaysPromptAndDropdown() {
    composeTestRule.setContent {
        RegionalSettingsScreen(
            currentLanguage = "Español",
            onLanguageChange = {},
            onNavigate = {},
            selectedRoute = "home",
            onBack = {},
            onSave = {}
        )
    }

    composeTestRule.onNodeWithText("¿Qué actividad estás realizando?").assertIsDisplayed()
    composeTestRule.onNodeWithText("Idioma").assertIsDisplayed()
    composeTestRule.onNodeWithText("Guardar").assertIsDisplayed()
}
