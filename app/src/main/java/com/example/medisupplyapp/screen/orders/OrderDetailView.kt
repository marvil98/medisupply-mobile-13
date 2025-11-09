package com.example.medisupplyapp.screen.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.InfoRowWithIcon
import com.example.medisupplyapp.components.ProductItemCard
import com.example.medisupplyapp.components.SimpleTopBar
import com.example.medisupplyapp.data.model.OrderDetail
import com.example.medisupplyapp.screen.LoadingScreen
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun OrderDetailScreen(
    orderId: Int,
    viewModel: OrderDetailViewModel = viewModel(),
    onBack: () -> Unit
) {
    LaunchedEffect(orderId) {
        viewModel.loadOrderDetail(orderId)
    }

    val uiState = viewModel.uiState

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.order_detail),
                    onBack = onBack
                )
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { paddingValues ->
            when (uiState) {
                is OrderDetailUiState.Loading -> {
                    LoadingScreen()
                }
                is OrderDetailUiState.Success -> {
                    OrderDetailContent(
                        orderDetail = uiState.orderDetail,
                        paddingValues = paddingValues
                    )
                }
                is OrderDetailUiState.Error -> {
                    ErrorDetailScreen(
                        paddingValues = paddingValues,
                        message = uiState.message,
                        onRetry = { viewModel.retry(orderId) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrderDetailContent(
    orderDetail: OrderDetail,
    paddingValues: PaddingValues
) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Espaciador superior
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // ============================================
        // CARD: INFORMACIÓN GENERAL DEL PEDIDO
        // ============================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Encabezado con ID y Estado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(R.string.order)} #${orderDetail.id}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Estado con círculo de color
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(orderDetail.status.color, CircleShape)
                            )
                            Text(
                                text = stringResource(orderDetail.status.displayName),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF666666)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFE0E0E0))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Información con iconos
                    InfoRowWithIcon(
                        icon = Icons.Default.CalendarToday,
                        label = stringResource(R.string.creation_date),
                        value = sdf.format(orderDetail.creationDate)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRowWithIcon(
                        icon = Icons.Default.CalendarToday,
                        label = stringResource(R.string.last_update),
                        value = sdf.format(orderDetail.lastUpdate)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fecha estimada de entrega (si existe)
                    orderDetail.estimatedReleaseDate?.let { releaseDate ->
                        InfoRowWithIcon(
                            icon = Icons.Default.CalendarToday,
                            label = stringResource(R.string.estimated_delivery),
                            value = sdf.format(releaseDate),
                            valueColor = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    InfoRowWithIcon(
                        icon = Icons.Default.LocationOn,
                        label = stringResource(R.string.delivery_address),
                        value = orderDetail.address
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRowWithIcon(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.client_id),
                        value = orderDetail.clientName
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRowWithIcon(
                        icon = Icons.Default.Person,
                        label = stringResource(R.string.seller_id),
                        value = orderDetail.sellerName
                    )
                }
            }
        }

        // ============================================
        // SECCIÓN: PRODUCTOS
        // ============================================
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Productos",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.products),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "(${orderDetail.items.size})",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }

        // Lista de productos
        items(orderDetail.items) { item ->
            ProductItemCard(item = item)
        }

        // ============================================
        // CARD: RESUMEN FINANCIERO
        // ============================================
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Valor total",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.total_order),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = currencyFormat.format(orderDetail.orderValue),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        // Espaciador inferior
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}