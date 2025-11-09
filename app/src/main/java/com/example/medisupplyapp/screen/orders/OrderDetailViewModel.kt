package com.example.medisupplyapp.screen.orders

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.OrderDetail
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.OrdersRepository
import kotlinx.coroutines.launch

sealed class OrderDetailUiState {
    object Loading : OrderDetailUiState()
    data class Success(val orderDetail: OrderDetail) : OrderDetailUiState()
    data class Error(val message: String) : OrderDetailUiState()
}

class OrderDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OrdersRepository(
        api = ApiConnection.api
        // Agrega tu DataStore si lo necesitas
    )

    var uiState by mutableStateOf<OrderDetailUiState>(OrderDetailUiState.Loading)
        private set

    fun loadOrderDetail(orderId: Int) {
        viewModelScope.launch {
            uiState = OrderDetailUiState.Loading

            Log.d("ORDER_DETAIL", "Cargando detalle del pedido: $orderId")

            // Llama al repository que retorna Result<OrderDetail>
            val result = repository.getOrderDetail(orderId)

            result.onSuccess { orderDetail ->
                Log.d("ORDER_DETAIL", "Pedido cargado exitosamente")
                uiState = OrderDetailUiState.Success(orderDetail)
            }.onFailure { exception ->
                Log.e("ORDER_DETAIL", "Error al cargar pedido: ${exception.message}", exception)
                uiState = OrderDetailUiState.Error(
                    exception.message ?: "Error desconocido al cargar el pedido"
                )
            }
        }
    }

    fun retry(orderId: Int) {
        loadOrderDetail(orderId)
    }
}