package com.example.medisupplyapp.screen.orders

import android.app.Application
import android.util.Log // ¡Añade este import!
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import com.example.medisupplyapp.data.remote.repository.OrdersRepository
import kotlinx.coroutines.launch

class FollowOrdersViewModel(application: Application)  : AndroidViewModel(application) {

    private val repository: OrdersRepository = OrdersRepository(api = ApiConnection.api)
    private val userRepository = ClientRepository(
        api = ApiConnection.api_users,
        application
    )

    var clientIdState by mutableStateOf<Int?>(null)
    private set

    var ordersState by mutableStateOf<OrdersUiState>(OrdersUiState.Loading)
        private set

    init {
        Log.d("ORDER_API", "ViewModel inicializado. Intentando cargar órdenes.")
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            val clientId = userRepository.getClientId()
            clientIdState = clientId
            ordersState = OrdersUiState.Loading
            repository.getOrders(clientId!!)
                .onSuccess { orders ->
                    ordersState = if (orders.isEmpty()) {
                        OrdersUiState.Empty
                    } else {
                        // Opcional: Log de éxito para confirmar que llegamos hasta aquí
                        Log.d("ORDER_API", "✅ Órdenes cargadas exitosamente: ${orders.size} elementos.")
                        OrdersUiState.Success(orders)
                    }
                }
                .onFailure { error ->
                    // 🚀 CRÍTICO: Imprimir la traza completa (el 'error' es el Throwable)
                    Log.e("ORDER_API", "❌ Fallo en la llamada a la API de órdenes", error)
                    ordersState = OrdersUiState.Error(error.message ?: "Error desconocido")
                }
        }
    }
}

sealed class OrdersUiState {
    object Loading : OrdersUiState()
    data class Success(val orders: List<Order>) : OrdersUiState()
    data class Error(val message: String) : OrdersUiState()
    object Empty : OrdersUiState()
}
