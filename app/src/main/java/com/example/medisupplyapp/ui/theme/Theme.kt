package com.tuempresa.medisupply.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val MediSupplyColorScheme = lightColorScheme(
    primary = Color(0xFF2260FF),
    secondary = Color(0xFFCAD6FF),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

val LeagueSpartanFontFamily = FontFamily(
    Font(R.font.league_spartan_regular, FontWeight.Normal),
    Font(R.font.league_spartan_medium, FontWeight.Medium),
    Font(R.font.league_spartan_bold, FontWeight.Bold)
)

private val MediSupplyTypography = Typography(
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    bodyMedium = TextStyle(fontSize = 16.sp),
    bodySmall = TextStyle(fontSize = 14.sp, color = Color.Gray)
)

@Composable
fun MediSupplyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MediSupplyColorScheme,
        typography = MediSupplyTypography,
        content = content
    )
}
