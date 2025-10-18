package com.example.medisupplyapp.components

import androidx.compose.ui.text.font.FontWeight
import org.junit.Assert.assertEquals
import org.junit.Test

fun getDropdownDisplayText(selected: String?, placeholder: String): String {
    return selected ?: placeholder
}

fun getOptionFontWeight(option: String, selected: String?): FontWeight {
    return if (option == selected) FontWeight.Bold else FontWeight.Normal
}

class DropdownUtilsTest {
    @Test
    fun `given no selection when getting display text then returns placeholder`() {
        val result = getDropdownDisplayText(null, "Selecciona una opción")
        assertEquals("Selecciona una opción", result)
    }

    @Test
    fun `given selection when getting display text then returns selected value`() {
        val result = getDropdownDisplayText("Option A", "Selecciona una opción")
        assertEquals("Option A", result)
    }

    @Test
    fun `given selected option when getting font weight then returns bold`() {
        val result = getOptionFontWeight("Option A", "Option A")
        assertEquals(FontWeight.Bold, result)
    }

    @Test
    fun `given non-selected option when getting font weight then returns normal`() {
        val result = getOptionFontWeight("Option B", "Option A")
        assertEquals(FontWeight.Normal, result)
    }
}
