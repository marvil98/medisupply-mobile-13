package com.example.medisupplyapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.data.model.Evidence
import com.example.medisupplyapp.data.model.StatusUpdate

@Composable
fun EvidenceCard(
    evidence: Evidence,
    modifier: Modifier = Modifier,
    onRetry: (Evidence) -> Unit,
    onDelete: (Evidence) -> Unit
) {
    val errorColor = MaterialTheme.colorScheme.error
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (evidence.thumbnail != null) {
                Image(
                    bitmap = evidence.thumbnail.asImageBitmap(),
                    contentDescription = evidence.nameFile,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                when (evidence.statusUpdate) {
                    StatusUpdate.SUBIENDO -> {
                        LinearProgressIndicator(
                            progress = evidence.progress,
                            color = primaryColor,
                            trackColor = Color.LightGray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Subiendo... ${(evidence.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = primaryColor
                        )
                    }
                    StatusUpdate.ERROR -> {
                        LinearProgressIndicator(
                            progress = 1f,
                            color = errorColor,
                            trackColor = errorColor.copy(alpha = 0.3f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Error al subir. Intenta nuevamente.",
                            color = errorColor,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    StatusUpdate.COMPLETADO -> {
                        LinearProgressIndicator(
                            progress = 1f,
                            color = primaryColor,
                            trackColor = primaryColor.copy(alpha = 0.3f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                    StatusUpdate.PENDIENTE -> {
                        Text(
                            text = "Lista para subir (${evidence.type})",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End
            ) {
                AnimatedVisibility(visible = evidence.statusUpdate == StatusUpdate.ERROR) {
                    IconButton(
                        onClick = { onRetry(evidence) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_refresh),
                            contentDescription = "Reintentar",
                            tint = primaryColor
                        )
                    }
                }

                IconButton(
                    onClick = { onDelete(evidence) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_trash),
                        contentDescription = "Eliminar",
                        tint = errorColor
                    )
                }
            }
        }
    }
}