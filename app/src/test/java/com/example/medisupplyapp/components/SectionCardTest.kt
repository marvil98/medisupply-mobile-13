package com.example.medisupplyapp.components

import org.junit.Assert.*
import org.junit.Test

/**
 * Determines if the card should be clickable.
 */
fun isCardClickable(centered: Boolean, hasClickHandler: Boolean): Boolean {
    return centered && hasClickHandler
}

/**
 * Returns the list of options to render.
 */
fun getVisibleOptions(options: List<String>): List<String> {
    return options.filter { it.isNotBlank() }
}

class SectionCardUtilsTest {

    @Test
    fun `card is clickable when centered and has handler`() {
        val result = isCardClickable(centered = true, hasClickHandler = true)
        assertTrue(result)
    }

    @Test
    fun `card is not clickable when not centered`() {
        val result = isCardClickable(centered = false, hasClickHandler = true)
        assertFalse(result)
    }

    @Test
    fun `card is not clickable when handler is null`() {
        val result = isCardClickable(centered = true, hasClickHandler = false)
        assertFalse(result)
    }

    @Test
    fun `getVisibleOptions filters out blank options`() {
        val input = listOf("Option A", "", "Option B", " ")
        val expected = listOf("Option A", "Option B")
        val result = getVisibleOptions(input)
        assertEquals(expected, result)
    }
}