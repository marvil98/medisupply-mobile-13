package com.tuempresa.medisupply.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material3.Typography
import com.example.medisupplyapp.R


private val MediSupplyColorScheme = lightColorScheme(
    primary = Color(0xFF2260FF),
    secondary = Color(0xFFCAD6FF),
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color(0xFF809CFF),
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFEB5757)
)

val LeagueSpartan = FontFamily(
    Font(R.font.league_spartan_extralight, FontWeight.ExtraLight),
    Font(R.font.league_spartan_light, FontWeight.Light),
    Font(R.font.league_spartan_regular, FontWeight.Normal),
    Font(R.font.league_spartan_medium, FontWeight.Medium),
    Font(R.font.league_spartan_semibold, FontWeight.SemiBold),
    Font(R.font.league_spartan_bold, FontWeight.Bold),
    Font(R.font.league_spartan_extrabold, FontWeight.ExtraBold),
    Font(R.font.league_spartan_black, FontWeight.Black)
)

private val MediSupplyTypography = Typography(
    titleLarge = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    titleMedium = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, fontFamily = LeagueSpartan),
    bodyMedium = TextStyle(fontSize = 16.sp, fontFamily = LeagueSpartan),
    bodySmall = TextStyle(fontSize = 14.sp,  fontFamily = LeagueSpartan),
    labelSmall = TextStyle(fontSize = 12.sp,  fontFamily = LeagueSpartan),
    labelMedium = TextStyle(fontSize = 14.sp,  fontFamily = LeagueSpartan),
    labelLarge = TextStyle(fontSize = 22.sp,  fontFamily = LeagueSpartan),
    headlineLarge = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    headlineMedium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    headlineSmall = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    displayLarge = TextStyle(fontSize = 57.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    displayMedium = TextStyle(fontSize = 45.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    displaySmall = TextStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    titleSmall = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = LeagueSpartan),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontFamily = LeagueSpartan)
)

@Composable
fun MediSupplyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MediSupplyColorScheme,
        typography = MediSupplyTypography,
        content = content
    )
}
