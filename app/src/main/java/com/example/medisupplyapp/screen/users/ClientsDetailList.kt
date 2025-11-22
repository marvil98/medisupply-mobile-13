package com.example.medisupplyapp.screen.users

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.*
import com.example.medisupplyapp.components.*
import com.example.medisupplyapp.data.model.*
import com.example.medisupplyapp.R
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.components.*
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ClientDetailScreen(
    client: Client,
    onBack: () -> Unit,
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    viewModel: ClientViewmodel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val clientState = viewModel.clientState
    val orderState = viewModel.ordersState
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    LaunchedEffect(client.userId) {
        viewModel.loadClientInfo(client.userId)
        viewModel.loadOrders(client.userId)
    }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(title = stringResource(R.string.users), onBack = onBack)
            },
            bottomBar = {
                FooterNavigation(selectedRoute, onNavigate)
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.client_detail),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.contact_info),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = client.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        when (clientState) {
                            is ClientUiState.Loading -> CircularProgressIndicator()
                            is ClientUiState.Success -> {
                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(R.string.label_client) + " ")
                                        }
                                        append("${clientState.client.userName} ${clientState.client.lastName}")
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(R.string.label_phone) + " ")
                                        }
                                        append(client.phone)
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                            append(stringResource(R.string.label_email) + " ")
                                        }
                                        append(clientState.client.email)
                                    },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            is ClientUiState.Error -> Text(stringResource(R.string.no_data))
                            ClientUiState.Empty -> Text(stringResource(R.string.no_data))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.order_history),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (orderState) {
                    is OrdersState.Loading -> {
                        LoadingScreen()
                    }
                    is OrdersState.Success -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(orderState.orders.size) { i ->
                                OrderCard(
                                    order = orderState.orders[i],
                                    onClick = {}
                                )
                            }
                        }
                    }
                    is OrdersState.Empty -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFD9D9D9),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.no_data_orders),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    is OrdersState.Error -> {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFD9D9D9),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.error_order_history),
                                    style = MaterialTheme.typography.headlineSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.statement),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        when (clientState) {
                            is ClientUiState.Loading -> CircularProgressIndicator()
                            is ClientUiState.Success -> {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.available),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = currencyFormat.format(clientState.client.balance.toDouble()),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                            is ClientUiState.Error -> Text(stringResource(R.string.no_data))
                            ClientUiState.Empty -> Text(stringResource(R.string.no_data))
                        }
                    }
                }
            }
        }
    }
}
