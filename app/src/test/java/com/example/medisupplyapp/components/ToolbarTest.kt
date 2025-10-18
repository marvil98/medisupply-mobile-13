package com.example.medisupplyapp.components
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Returns the font size used in the top bar title.
 */
fun getTopBarTitleFontSize(): TextUnit = 24.sp

class TopBarUtilsTest {

    @Test
    fun `getTopBarTitleFontSize returns 24sp`() {
        val result = getTopBarTitleFontSize()
        assertEquals(24.sp, result)
    }
}