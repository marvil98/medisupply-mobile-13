package com.example.medisupplyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DateSelector(
    label: String,
    selectedDate: Date?,
    isError: Boolean,
    onClicked: () -> Unit
) {

    val dateText = selectedDate?.let {
        // Define el formato deseado (dd/MM/yyyy) y usa la localización adecuada
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // Formatea la fecha seleccionada
        formatter.format(it)
    } ?:  stringResource(R.string.select_date)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                // Aplicamos el clic al Box, que abarca toda el área del TextField.
                // Esto garantiza que el toque se detecte ANTES de la lógica interna del TextField.
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClicked
                )
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .border(
                    width = 0.dp,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            OutlinedTextField(
                // --- Usar la fecha formateada o el placeholder ---
                value = dateText,
                onValueChange = { /* Solo lectura */ },
                readOnly = true, // Es fundamental mantener esto para evitar el teclado
                enabled = false, // Deshabilitar evita que capture eventos de foco/teclado
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = "Seleccionar fecha",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                isError = isError,
                // El Modifier del TextField ahora solo necesita rellenar el Box padre
                modifier = Modifier.fillMaxSize(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent, // Fondo transparente ya que el Box lo maneja
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    // Usamos el color de texto para el estado 'disabled'
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    errorSupportingTextColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = if (dateText == stringResource(R.string.select_date)) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primary,
                ),
            )


        }
    }
}