package com.example.medisupplyapp.screen

import org.junit.Assert.*
import org.junit.Test

/**
 * Lógica de configuración regional para selección de idioma.
 */
object RegionalSettingsLogic {

    private val supportedLanguages = listOf("Español", "English")

    /**
     * Devuelve la lista de idiomas soportados.
     */
    fun getLanguageOptions(): List<String> = supportedLanguages

    /**
     * Retorna true si el idioma es válido (no nulo y dentro de los soportados).
     */
    fun isLanguageValid(language: String?): Boolean {
        return language != null && supportedLanguages.contains(language)
    }

    /**
     * Retorna true si debe mostrarse un error (idioma nulo o no válido).
     */
    fun shouldShowLanguageError(language: String?): Boolean {
        return language == null || !supportedLanguages.contains(language)
    }
}

/**
 * Pruebas unitarias para la lógica de configuración regional.
 */
class RegionalSettingsUtilsTest {

    @Test
    fun `given a valid language when checking validity then returns true`() {
        val result = RegionalSettingsLogic.isLanguageValid("Español")
        assertTrue(result)
    }

    @Test
    fun `given null language when checking validity then returns false`() {
        val result = RegionalSettingsLogic.isLanguageValid(null)
        assertFalse(result)
    }

    @Test
    fun `given unsupported language when checking validity then returns false`() {
        val result = RegionalSettingsLogic.isLanguageValid("Français")
        assertFalse(result)
    }

    @Test
    fun `given empty string when checking validity then returns false`() {
        val result = RegionalSettingsLogic.isLanguageValid("")
        assertFalse(result)
    }

    @Test
    fun `given null language when checking error state then returns true`() {
        val result = RegionalSettingsLogic.shouldShowLanguageError(null)
        assertTrue(result)
    }

    @Test
    fun `given unsupported language when checking error state then returns true`() {
        val result = RegionalSettingsLogic.shouldShowLanguageError("Italiano")
        assertTrue(result)
    }

    @Test
    fun `given valid language when checking error state then returns false`() {
        val result = RegionalSettingsLogic.shouldShowLanguageError("English")
        assertFalse(result)
    }

    @Test
    fun `when retrieving supported languages then returns Español and English`() {
        val result = RegionalSettingsLogic.getLanguageOptions()
        val expected = listOf("Español", "English")
        assertEquals(expected, result)
    }
}
