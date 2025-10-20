package com.example.medisupplyapp.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FloatingToast(
    message: String,
    type: ToastType,
    visible: Boolean,
    onDismiss: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .wrapContentHeight()
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp,
            color = when (type) {
                ToastType.SUCCESS -> MaterialTheme.colorScheme.onTertiary
                ToastType.ERROR -> MaterialTheme.colorScheme.onError
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = when (type) {
                        ToastType.SUCCESS -> MaterialTheme.colorScheme.surfaceVariant
                        ToastType.ERROR -> MaterialTheme.colorScheme.error
                    },
                    modifier = Modifier.weight(2f)
                )

                Icon(
                    imageVector = when (type) {
                        ToastType.SUCCESS -> Icons.Default.Check
                        ToastType.ERROR -> Icons.Default.Close
                    },
                    contentDescription = null,
                    tint = when (type) {
                        ToastType.SUCCESS -> MaterialTheme.colorScheme.surfaceVariant
                        ToastType.ERROR -> MaterialTheme.colorScheme.error
                    },
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }
}

enum class ToastType {
    SUCCESS, ERROR
}