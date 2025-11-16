package com.example.medisupplyapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.medisupplyapp.R

@Composable
fun MenuDialog(
    userName: String,
    userRole: String,
    onDismiss: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        // Contenedor del modal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = onDismiss,
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                ),
            contentAlignment = Alignment.TopEnd
        ) {
            // Card del menú (posicionado arriba a la izquierda)
            Card(
                modifier = Modifier
                    .padding(top = 100.dp, end = 16.dp)
                    .widthIn(max = 250.dp)
                    .clickable(
                        onClick = {},
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp)
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = userRole,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    // Divider antes de cerrar sesión
                    HorizontalDivider(
                        Modifier,
                        DividerDefaults.Thickness,
                        color = MaterialTheme.colorScheme.inverseSurface
                    )

                    // Botón de cerrar sesión
                    TextButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.inverseSurface
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.logout),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

