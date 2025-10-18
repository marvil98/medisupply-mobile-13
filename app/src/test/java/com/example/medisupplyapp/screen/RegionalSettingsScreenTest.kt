package com.example.medisupplyapp.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegionalSettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun regionalSettingsScreen_displaysCorrectContent() {
        // Arrange
        val currentLanguage = "Español"
        var languageChanged = ""
        var saved = false
        var navigated = ""
        var backPressed = false

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { languageChanged = it },
                onNavigate = { navigated = it },
                selectedRoute = "settings",
                onBack = { backPressed = true },
                onSave = { saved = true }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Idioma").assertIsDisplayed()
        composeTestRule.onNodeWithText("Español").assertIsDisplayed()
        composeTestRule.onNodeWithText("English").assertIsDisplayed()
    }

    @Test
    fun regionalSettingsScreen_languageSelectionWorks() {
        // Arrange
        val currentLanguage = "Español"
        var languageChanged = ""

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { languageChanged = it },
                onNavigate = { },
                selectedRoute = "settings",
                onBack = { },
                onSave = { }
            )
        }

        // Click on dropdown to open it
        composeTestRule.onNodeWithText("Español").performClick()
        
        // Select English
        composeTestRule.onNodeWithText("English").performClick()

        // Assert
        assert(languageChanged == "English")
    }

    @Test
    fun regionalSettingsScreen_saveButtonEnabledWhenLanguageSelected() {
        // Arrange
        val currentLanguage = "Español"
        var saved = false

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { },
                onNavigate = { },
                selectedRoute = "settings",
                onBack = { },
                onSave = { saved = true }
            )
        }

        // Assert - Save button should be enabled when language is selected
        composeTestRule.onNodeWithText("Guardar").assertIsEnabled()
        
        // Click save button
        composeTestRule.onNodeWithText("Guardar").performClick()
        
        // Assert
        assert(saved)
    }

    @Test
    fun regionalSettingsScreen_backButtonWorks() {
        // Arrange
        val currentLanguage = "Español"
        var backPressed = false

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { },
                onNavigate = { },
                selectedRoute = "settings",
                onBack = { backPressed = true },
                onSave = { }
            )
        }

        // Click back button (assuming it's in the top bar)
        composeTestRule.onNodeWithContentDescription("Navigate back").performClick()

        // Assert
        assert(backPressed)
    }

    @Test
    fun regionalSettingsScreen_initialLanguageIsSet() {
        // Arrange
        val currentLanguage = "English"

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { },
                onNavigate = { },
                selectedRoute = "settings",
                onBack = { },
                onSave = { }
            )
        }

        // Assert
        composeTestRule.onNodeWithText("English").assertIsDisplayed()
    }

    @Test
    fun regionalSettingsScreen_languageOptionsAreAvailable() {
        // Arrange
        val currentLanguage = "Español"

        // Act
        composeTestRule.setContent {
            RegionalSettingsScreen(
                currentLanguage = currentLanguage,
                onLanguageChange = { },
                onNavigate = { },
                selectedRoute = "settings",
                onBack = { },
                onSave = { }
            )
        }

        // Click on dropdown to open it
        composeTestRule.onNodeWithText("Español").performClick()

        // Assert both language options are available
        composeTestRule.onNodeWithText("Español").assertIsDisplayed()
        composeTestRule.onNodeWithText("English").assertIsDisplayed()
    }
}
