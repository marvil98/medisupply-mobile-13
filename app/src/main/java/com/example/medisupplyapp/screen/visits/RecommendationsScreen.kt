package com.example.medisupplyapp.screen.visits

import com.example.medisupplyapp.components.SimpleTopBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.data.model.Recommendation
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme

@Composable
fun RecommendationsScreen(
    recommendations: List<Recommendation>,
    onBackToHome: () -> Unit
) {
    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.visits),
                    onBack = onBackToHome,
                    showBackIcon = false
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(48.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onBackToHome,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    ) {
                        Text(stringResource(R.string.recommend))
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.recomendations),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                if (recommendations.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.no_recomendations),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                        )
                    }
                } else {
                    items(recommendations) { recommendation ->
                        RecommendationCard(recommendation = recommendation)
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(recommendation: Recommendation) {
    MediSupplyTheme {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = recommendation.product_name ?: stringResource(R.string.product_name_unavailable),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    recommendation.score?.let { score ->
                        recommendation.score?.let { score ->
                            Text(
                                text = stringResource(R.string.score_label, score),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "SKU: ${recommendation.product_sku ?: stringResource(R.string.no_available)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                recommendation.reasoning?.let { reason ->
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.reasoning_label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}