package com.example.medisupplyapp.screen

import org.junit.Assert.*
import org.junit.Test

/**
 * Returns true if the selected language is valid.
 */
fun isLanguageValid(language: String?): Boolean {
    return language != null
}

/**
 * Returns true if the language selection should show an error.
 */
fun shouldShowLanguageError(language: String?): Boolean {
    return language == null
}

/**
 * Returns the list of supported languages.
 */
fun getSupportedLanguages(): List<String> {
    return listOf("Español", "English")
}

class RegionalSettingsUtilsTest {

    @Test
    fun `given a valid language when checking validity then returns true`() {
        val language = "Español"
        val result = isLanguageValid(language)
        assertTrue(result)
    }

    @Test
    fun `given a null language when checking validity then returns false`() {
        val language: String? = null
        val result = isLanguageValid(language)
        assertFalse(result)
    }

    @Test
    fun `given a null language when checking error state then returns true`() {
        val language: String? = null
        val result = shouldShowLanguageError(language)
        assertTrue(result)
    }

    @Test
    fun `given a valid language when checking error state then returns false`() {
        val language = "English"
        val result = shouldShowLanguageError(language)
        assertFalse(result)
    }

    @Test
    fun `given supported languages when retrieving list then returns Español and English`() {
        val result = getSupportedLanguages()
        val expected = listOf("Español", "English")
        assertEquals(expected, result)
    }
}