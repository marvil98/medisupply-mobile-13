package com.example.medisupplyapp.screen.visits

import android.net.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.OrdersListContent
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.screen.LoadingScreen
import com.example.medisupplyapp.screen.orders.ErrorOrdersScreen
import com.example.medisupplyapp.screen.orders.OrdersUiState
import com.tuempresa.medisupply.ui.components.FooterNavigation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.time.*
import java.util.*

enum class TipoEvidencia {
    FOTO,
    VIDEO,
    GALERIA
}

data class Evidencia(
    val id: String = UUID.randomUUID().toString(),
    val tipo: TipoEvidencia,
    val nombreArchivo: String,
    val uri: Uri,
    val fecha: LocalDateTime = LocalDateTime.now()
)

@Composable
fun BotonEvidencia(texto: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(texto)
    }
}

@Composable
fun EvidenciaCard(evidencia: Evidencia) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tipo: ${evidencia.tipo}")
            Text("Archivo: ${evidencia.nombreArchivo}")
        }
    }
}

@Composable
fun RegisterEvidenceScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    selectedRoute: String,
) {
    var opcionesVisibles by remember { mutableStateOf(false) }
    val evidencias = remember { mutableStateListOf<Evidencia>() }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(com.example.medisupplyapp.R.string.orders),
                    onBack = onBack
                ) },
            bottomBar = { FooterNavigation(selectedRoute, onNavigate) },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)) {
                Text(
                    "Agregar Evidencias",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Card principal
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (evidencias.isEmpty()) {
                            // Estado vacío
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(64.dp)
                                )
                                Text("Aún no has agregado evidencias.", color = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Agregar evidencia",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable {
                                        opcionesVisibles = !opcionesVisibles
                                    }
                                )
                            }
                        } else {
                            // Lista de evidencias
                            evidencias.forEach { evidencia ->
                                EvidenciaCard(evidencia)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        // Botones desplegables
                    }
                }

                Text(
                    text = "Agregar evidencia",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        opcionesVisibles = !opcionesVisibles
                    }
                )

                AnimatedVisibility(visible = opcionesVisibles) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))
                        BotonEvidencia("Tomar foto", {})
                        BotonEvidencia("Grabar video", {  })
                        BotonEvidencia("Seleccionar de galería", {  })
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón Finalizar
                Button(
                    onClick = { /* Acción finalizar */ },
                    enabled = evidencias.isNotEmpty(),
                    modifier = Modifier
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                    )
                ) {
                    Text("Finalizar evidencias")
                }
            }
        }
    }
}
