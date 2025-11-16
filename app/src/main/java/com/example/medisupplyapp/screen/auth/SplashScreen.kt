package com.example.medisupplyapp.screen.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import kotlinx.coroutines.delay

// ============================================
// PANTALLA INICIAL - Auto-navega después de 2 segundos
// ============================================
@Composable
fun SplashScreenWithAutoNavigation(
    viewModel: SplashViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSplash: () -> Unit  // Navega a SplashScreen
) {

    var isCheckingSession by remember { mutableStateOf(true) }
    // Auto-navegar después de 2 segundos
    LaunchedEffect(Unit) {
        delay(2000)
        val hasValidSession = viewModel.checkSession()

        if (hasValidSession) {
            // Hay sesión válida → Ir directo a Home
            onNavigateToHome()
        } else {
            // No hay sesión → Ir a SplashScreen con botón
            onNavigateToSplash()
        }

        isCheckingSession = false    }


    MediSupplyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Logo con animación
                    Image(
                        painter = painterResource(id = R.drawable.ic_white_logo),
                        contentDescription = "MediSupply Logo",
                        modifier = Modifier
                            .size(200.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Texto "Medi"
                    Text(
                        text = "Medi",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background,
                        letterSpacing = 1.sp
                    )

                    // Texto "Supply"
                    Text(
                        text = "Supply",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// ============================================
// PANTALLA CON BOTÓN "Iniciar sesión"
// ============================================
@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    MediSupplyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "MediSupply Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Nombre de la app
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Medi",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Supply",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))

                // Botón "Iniciar sesión"
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.login),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}

