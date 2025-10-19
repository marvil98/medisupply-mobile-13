package com.example.medisupplyapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.medisupplyapp.R
import com.example.medisupplyapp.data.model.Order

@Composable
fun OrdersListContent(
    orders: List<Order>,
    paddingValues: PaddingValues,
    onNavigate: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(5.dp))
        }
        item{
            RoundedButton(
                title = stringResource(R.string.follow_order),
                navigation = "create_order",
                onOptionClick = { onNavigate(it) }
            )
        }
        item {
            Text(
                text = stringResource(R.string.order_history),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Lista de pedidos
        items(orders.size) { i ->
            OrderCard(
                order = orders[i],
            )
        }
    }
}