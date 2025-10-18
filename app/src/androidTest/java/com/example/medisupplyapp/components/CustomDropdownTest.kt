
package com.example.medisupplyapp.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class CustomDropdownTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val options = listOf("Option A", "Option B", "Option C")

    @Test
    fun givenNoSelection_whenRendered_thenShowsLabelAndPlaceholder() {
        // Given
        composeTestRule.setContent {
            CustomDropdown(
                label = "Select Item",
                options = options,
                selected = null,
                onSelect = {},
                hasError = false
            )
        }

        // Then
        composeTestRule.onNodeWithText("Select Item").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecciona una opción").assertIsDisplayed()
    }

    @Test
    fun givenDropdown_whenUserSelectsOption_thenSelectionIsUpdated() {
        // Given
        var selectedOption: String? = null

        composeTestRule.setContent {
            CustomDropdown(
                label = "Choose",
                options = options,
                selected = selectedOption,
                onSelect = { selectedOption = it },
                hasError = false
            )
        }

        // When
        composeTestRule.onNodeWithText("Selecciona una opción").performClick()
        composeTestRule.onNodeWithText("Option B").performClick()

        // Then
        composeTestRule.setContent {
            CustomDropdown(
                label = "Choose",
                options = options,
                selected = selectedOption,
                onSelect = { selectedOption = it },
                hasError = false
            )
        }

        composeTestRule.onNodeWithText("Option B").assertIsDisplayed()
    }

    @Test
    fun givenHasErrorTrue_whenRendered_thenShowsErrorMessage() {
        // Given
        composeTestRule.setContent {
            CustomDropdown(
                label = "Field",
                options = options,
                selected = null,
                onSelect = {},
                hasError = true
            )
        }

        // Then
        composeTestRule.onNodeWithText("Campo requerido").assertIsDisplayed()
    }
}
