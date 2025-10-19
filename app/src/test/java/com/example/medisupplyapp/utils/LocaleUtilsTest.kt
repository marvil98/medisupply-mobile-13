package com.example.medisupplyapp.utils

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class LocaleUtilsTest {

    @Test
    fun localeCreation_withSpanishCode_createsCorrectLocale() {
        // Arrange
        val languageCode = "es"
        
        // Act
        val locale = Locale(languageCode)
        
        // Assert
        assertEquals("es", locale.language)
        assertEquals("", locale.country)
    }

    @Test
    fun localeCreation_withEnglishCode_createsCorrectLocale() {
        // Arrange
        val languageCode = "en"
        
        // Act
        val locale = Locale(languageCode)
        
        // Assert
        assertEquals("en", locale.language)
        assertEquals("", locale.country)
    }

    @Test
    fun localeCreation_withFrenchCode_createsCorrectLocale() {
        // Arrange
        val languageCode = "fr"
        
        // Act
        val locale = Locale(languageCode)
        
        // Assert
        assertEquals("fr", locale.language)
        assertEquals("", locale.country)
    }

    @Test
    fun localeCreation_withCountryCode_createsCorrectLocale() {
        // Arrange
        val languageCode = "es"
        val countryCode = "ES"
        
        // Act
        val locale = Locale(languageCode, countryCode)
        
        // Assert
        assertEquals("es", locale.language)
        assertEquals("ES", locale.country)
    }

    @Test
    fun localeGetDisplayName_returnsCorrectDisplayName() {
        // Arrange
        val locale = Locale("es")
        
        // Act
        val displayName = locale.getDisplayName()
        
        // Assert
        assertNotNull(displayName)
        assertTrue(displayName.isNotEmpty())
    }

    @Test
    fun localeGetDisplayLanguage_returnsCorrectLanguage() {
        // Arrange
        val locale = Locale("en")
        
        // Act
        val displayLanguage = locale.getDisplayLanguage()
        
        // Assert
        assertNotNull(displayLanguage)
        assertTrue(displayLanguage.isNotEmpty())
    }

    @Test
    fun localeSetDefault_changesDefaultLocale() {
        // Arrange
        val originalDefault = Locale.getDefault()
        val newLocale = Locale("fr")
        
        // Act
        Locale.setDefault(newLocale)
        
        // Assert
        assertEquals("fr", Locale.getDefault().language)
        
        // Restore original locale
        Locale.setDefault(originalDefault)
    }

    @Test
    fun localeToString_returnsCorrectString() {
        // Arrange
        val locale = Locale("es", "ES")
        
        // Act
        val localeString = locale.toString()
        
        // Assert
        assertEquals("es_ES", localeString)
    }
}
