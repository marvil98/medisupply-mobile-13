package com.example.medisupplyapp.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import io.mockk.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [31])
class LocaleUtilsTest {

    private lateinit var mockContext: Context
    private lateinit var mockResources: Resources
    private lateinit var mockConfiguration: Configuration

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockResources = mockk(relaxed = true)
        mockConfiguration = mockk(relaxed = true)

        every { mockContext.resources } returns mockResources
        every { mockResources.configuration } returns mockConfiguration
        every { mockResources.displayMetrics } returns mockk(relaxed = true)
    }

    @Test
    fun updateLocale_withSpanishCode_setsCorrectLocale() {
        // Arrange
        val languageCode = "es"
        val expectedLocale = Locale("es")

        // Act
        updateLocale(mockContext, languageCode)

        // Assert
        verify { mockConfiguration.setLocale(expectedLocale) }
        verify { mockResources.updateConfiguration(mockConfiguration, any()) }
    }

    @Test
    fun updateLocale_withEnglishCode_setsCorrectLocale() {
        // Arrange
        val languageCode = "en"
        val expectedLocale = Locale("en")

        // Act
        updateLocale(mockContext, languageCode)

        // Assert
        verify { mockConfiguration.setLocale(expectedLocale) }
        verify { mockResources.updateConfiguration(mockConfiguration, any()) }
    }

    @Test
    fun updateLocale_withFrenchCode_setsCorrectLocale() {
        // Arrange
        val languageCode = "fr"
        val expectedLocale = Locale("fr")

        // Act
        updateLocale(mockContext, languageCode)

        // Assert
        verify { mockConfiguration.setLocale(expectedLocale) }
        verify { mockResources.updateConfiguration(mockConfiguration, any()) }
    }

    @Test
    fun updateLocale_callsResourcesUpdateConfiguration() {
        // Arrange
        val languageCode = "es"

        // Act
        updateLocale(mockContext, languageCode)

        // Assert
        verify { mockResources.updateConfiguration(any(), any()) }
    }

    @Test
    fun updateLocale_setsDefaultLocale() {
        // Arrange
        val languageCode = "es"
        val originalDefault = Locale.getDefault()

        // Act
        updateLocale(mockContext, languageCode)

        // Assert
        val newDefault = Locale.getDefault()
        assert(newDefault.language == "es")
        
        // Restore original locale
        Locale.setDefault(originalDefault)
    }
}
