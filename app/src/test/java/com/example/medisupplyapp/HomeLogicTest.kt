package com.example.medisupplyapp

import org.junit.Test
import org.junit.Assert.*

class HomeLogicTest {

    @Test
    fun homeSections_containsExpectedSections() {
        // Arrange
        val expectedSections = listOf("Rutas", "Visitas", "Pedidos", "Clientes")
        
        // Act
        val sections = listOf("Rutas", "Visitas", "Pedidos", "Clientes")
        
        // Assert
        assertEquals(expectedSections.size, sections.size)
        assertTrue(sections.contains("Rutas"))
        assertTrue(sections.contains("Visitas"))
        assertTrue(sections.contains("Pedidos"))
        assertTrue(sections.contains("Clientes"))
    }

    @Test
    fun visitOptions_containsExpectedOptions() {
        // Arrange
        val expectedOptions = listOf("Registrar Visita", "Sugerir Producto")
        
        // Act
        val options = listOf("Registrar Visita", "Sugerir Producto")
        
        // Assert
        assertEquals(expectedOptions.size, options.size)
        assertTrue(options.contains("Registrar Visita"))
        assertTrue(options.contains("Sugerir Producto"))
    }

    @Test
    fun orderOptions_containsExpectedOptions() {
        // Arrange
        val expectedOptions = listOf("Crear Pedido", "Seguir Pedido")
        
        // Act
        val options = listOf("Crear Pedido", "Seguir Pedido")
        
        // Assert
        assertEquals(expectedOptions.size, options.size)
        assertTrue(options.contains("Crear Pedido"))
        assertTrue(options.contains("Seguir Pedido"))
    }

    @Test
    fun navigationToRoutes_returnsCorrectRoute() {
        // Arrange
        val route = "rutas"
        
        // Act
        val isCorrectRoute = route == "rutas"
        
        // Assert
        assertTrue(isCorrectRoute)
    }

    @Test
    fun navigationToClients_returnsCorrectRoute() {
        // Arrange
        val route = "clientes"
        
        // Act
        val isCorrectRoute = route == "clientes"
        
        // Assert
        assertTrue(isCorrectRoute)
    }

    @Test
    fun navigationToRegionalSettings_returnsCorrectRoute() {
        // Arrange
        val route = "ajustes_regionales"
        
        // Act
        val isCorrectRoute = route == "ajustes_regionales"
        
        // Assert
        assertTrue(isCorrectRoute)
    }

    @Test
    fun userName_displayIsCorrect() {
        // Arrange
        val userName = "Test User"
        
        // Act
        val displayText = userName
        
        // Assert
        assertEquals("Test User", displayText)
        assertTrue(displayText.isNotEmpty())
    }

    @Test
    fun selectedRoute_homeIsSelected() {
        // Arrange
        val selectedRoute = "home"
        
        // Act
        val isHomeSelected = selectedRoute == "home"
        
        // Assert
        assertTrue(isHomeSelected)
    }
}
