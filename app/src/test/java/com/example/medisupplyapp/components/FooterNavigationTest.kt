package com.example.medisupplyapp.components


import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Returns true if the given route is currently selected.
 */
fun isRouteSelected(current: String, target: String): Boolean {
    return current == target
}

/**
 * Returns the list of valid navigation routes.
 */
fun getNavigationRoutes(): List<String> {
    return listOf("home", "routes", "visits", "orders", "users")
}

class NavigationUtilsTest {

    @Test
    fun `given matching route when checking selection then returns true`() {
        val result = isRouteSelected("home", "home")
        assertTrue(result)
    }

    @Test
    fun `given non-matching route when checking selection then returns false`() {
        val result = isRouteSelected("home", "orders")
        assertFalse(result)
    }

    @Test
    fun `getNavigationRoutes returns all expected routes`() {
        val expected = listOf("home", "routes", "visits", "orders", "users")
        val result = getNavigationRoutes()
        assertEquals(expected, result)
    }
}
