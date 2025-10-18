package com.example.medisupplyapp.components

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Returns the greeting color used in the header.
 */
fun getGreetingColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(0xFF2260FF)
}

/**
 * Returns the navigation route triggered by the globe icon.
 */
fun getRegionalSettingsRoute(): String {
    return "ajustes_regionales"
}

class HeaderUtilsTest {

    @Test
    fun `getGreetingColor returns expected blue tone`() {
        val expected = Color(0xFF2260FF)
        val result = getGreetingColor()
        assertEquals(expected, result)
    }

    @Test
    fun `getRegionalSettingsRoute returns ajustes_regionales`() {
        val result = getRegionalSettingsRoute()
        assertEquals("ajustes_regionales", result)
    }
}