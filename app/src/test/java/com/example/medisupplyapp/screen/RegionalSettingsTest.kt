package com.example.medisupplyapp.screen

import org.junit.Assert.*
import org.junit.Test


object RegionalSettingsLogic {

    fun isLanguageValid(language: String?): Boolean {
        return language != null
    }

    fun shouldShowLanguageError(language: String?): Boolean {
        return language == null
    }

    fun getSupportedLanguages(): List<String> {
        return listOf("Español", "English")
    }

    fun getLanguageOptions(): List<String> = listOf("Español", "English")
}

class RegionalSettingsUtilsTest {

    @Test
    fun `given a valid language when checking validity then returns true`() {
        assertTrue(RegionalSettingsLogic.isLanguageValid("Español"))
    }

    @Test
    fun `given a null language when checking validity then returns false`() {
        assertFalse(RegionalSettingsLogic.isLanguageValid(null))
    }

    @Test
    fun `given a null language when checking error state then returns true`() {
        assertTrue(RegionalSettingsLogic.shouldShowLanguageError(null))
    }

    @Test
    fun `given a valid language when checking error state then returns false`() {
        assertFalse(RegionalSettingsLogic.shouldShowLanguageError("English"))
    }

    @Test
    fun `given supported languages when retrieving list then returns Español and English`() {
        val result = RegionalSettingsLogic.getSupportedLanguages()
        val expected = listOf("Español", "English")
        assertEquals(expected, result)
    }
}
