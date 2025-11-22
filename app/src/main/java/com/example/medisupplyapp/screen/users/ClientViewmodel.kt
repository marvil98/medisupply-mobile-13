package com.example.medisupplyapp.screen.users

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.dto.ClientDetailResponse
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import com.example.medisupplyapp.data.remote.repository.OrdersRepository
import com.example.medisupplyapp.screen.orders.OrdersUiState
import kotlinx.coroutines.launch

class ClientViewmodel(application: Application) : AndroidViewModel(application) {
    private val repository: ClientRepository = ClientRepository(api = ApiConnection.api_users, application)
    private val repositoryOrders: OrdersRepository = OrdersRepository(api = ApiConnection.api)

    var clientsState by mutableStateOf<UsersUiState>(UsersUiState.Loading)
        private set

    var clientState by mutableStateOf<ClientUiState>(ClientUiState.Empty)
        private set

    var ordersState by mutableStateOf<OrdersState>(OrdersState.Loading)
        private set

    init {
        // 🚀 NUEVO: Log para confirmar que el ViewModel se inicializa
        Log.d("ORDER_API", "ViewModel inicializado. Intentando cargar órdenes.")
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            clientsState = UsersUiState.Loading
            try {
                val clients = repository.fecthClientsBySellerID(1)

                clientsState = if (clients.isEmpty()) {
                    UsersUiState.Empty
                } else {
                    UsersUiState.Success(clients)
                }
            } catch (e: Exception) {
                print("error ${e}")
                clientsState = UsersUiState.Error(
                    e.message ?: "Error desconocido al cargar clientes"
                )
            }
        }
    }

    fun loadClientInfo(userId: Int) {
        viewModelScope.launch {
            clientState = ClientUiState.Loading
            try {
                val response: ClientDetailResponse? = repository.fetchClientInfo(userId)
                clientState = if (response == null) {
                    ClientUiState.Empty
                } else {
                    ClientUiState.Success(response)
                }
            } catch (e: Exception) {
                clientState = ClientUiState.Error(
                    e.message ?: "Error desconocido al cargar cliente"
                )
            }
        }
    }

    fun loadOrders(clientId: Int) {
        viewModelScope.launch {
            ordersState = OrdersState.Loading
            repositoryOrders.getOrders(clientId)
                .onSuccess { orders ->
                    ordersState = if (orders.isEmpty()) {
                        OrdersState.Empty
                    } else {
                        // Opcional: Log de éxito para confirmar que llegamos hasta aquí
                        Log.d("ORDER_API", "✅ Órdenes cargadas exitosamente: ${orders.size} elementos.")
                        OrdersState.Success(orders)
                    }
                }
                .onFailure { error ->
                    // 🚀 CRÍTICO: Imprimir la traza completa (el 'error' es el Throwable)
                    Log.e("ORDER_API", "❌ Fallo en la llamada a la API de órdenes", error)
                    ordersState = OrdersState.Error(error.message ?: "Error desconocido")
                }
        }
    }
}

sealed class UsersUiState {
    object Loading : UsersUiState()
    data class Success(val users: List<Client>) : UsersUiState()
    data class Error(val message: String) : UsersUiState()
    object Empty : UsersUiState()
}

sealed class ClientUiState {
    object Loading : ClientUiState()
    data class Success(val client: ClientDetailResponse) : ClientUiState()
    data class Error(val message: String) : ClientUiState()
    object Empty : ClientUiState()
}

sealed class OrdersState {
    object Loading : OrdersState()
    data class Success(val orders: List<Order>) : OrdersState()
    data class Error(val message: String) : OrdersState()
    object Empty : OrdersState()
}

