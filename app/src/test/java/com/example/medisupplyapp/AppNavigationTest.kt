package com.example.medisupplyapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appNavigation_startsAtHomeScreen() {
        // Arrange
        val userName = "Test User"

        // Act
        composeTestRule.setContent {
            AppNavigation(userName = userName)
        }

        // Assert - Home screen should be displayed initially
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }

    @Test
    fun appNavigation_navigatesToRegionalSettings() {
        // Arrange
        val userName = "Test User"

        // Act
        composeTestRule.setContent {
            AppNavigation(userName = userName)
        }

        // Navigate to regional settings (assuming there's a way to navigate)
        // This test would need to be adjusted based on the actual navigation implementation
        composeTestRule.onNodeWithText("Ajustes Regionales").performClick()

        // Assert
        composeTestRule.onNodeWithText("Idioma").assertIsDisplayed()
    }

    @Test
    fun appNavigation_languageChangeUpdatesCorrectly() {
        // Arrange
        val userName = "Test User"

        // Act
        composeTestRule.setContent {
            AppNavigation(userName = userName)
        }

        // Navigate to regional settings
        composeTestRule.onNodeWithText("Ajustes Regionales").performClick()

        // Change language
        composeTestRule.onNodeWithText("Español").performClick()
        composeTestRule.onNodeWithText("English").performClick()

        // Assert - Language should be updated
        composeTestRule.onNodeWithText("English").assertIsDisplayed()
    }

    @Test
    fun appNavigation_saveButtonWorksInRegionalSettings() {
        // Arrange
        val userName = "Test User"

        // Act
        composeTestRule.setContent {
            AppNavigation(userName = userName)
        }

        // Navigate to regional settings
        composeTestRule.onNodeWithText("Ajustes Regionales").performClick()

        // Click save button
        composeTestRule.onNodeWithText("Guardar").performClick()

        // Assert - Should navigate back to home
        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    }
}
