package com.example.medisupplyapp.screen.orders

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.CreateOrderRequest
import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.model.ProductRequest
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import com.example.medisupplyapp.data.remote.repository.OrdersRepository
import com.example.medisupplyapp.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch

class CreateOrderViewModel : ViewModel() {
    var selectedClient by mutableStateOf<String?>(null)
    var clientError by mutableStateOf(false)
    var clients by mutableStateOf<List<Client>>(emptyList())
    var products by mutableStateOf<List<Product>>(emptyList())
    var selectedProducts by mutableStateOf<List<Product>>(emptyList())
    var productError by mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                val repo = ClientRepository(api = ApiConnection.api_users)
                val result = repo.fetchClients()
                clients = result
            } catch (e: Exception) {
            }

            try {
                val productRepo = ProductRepository(api = ApiConnection.api_products)
                products = productRepo.fetchProducts()
            } catch (e: Exception) {
            }
        }
    }

    fun validateClientOnDismiss() {
        clientError = selectedClient == null
    }

    fun isFormValid(): Boolean {
        return selectedClient != null && !clientError
    }

    fun createOrder(onSuccess: (orderId: Int, message: String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val clientId = selectedClient?.toIntOrNull()
                if (clientId == null) {
                    clientError = true
                    onError("Cliente inválido")
                    return@launch
                }

                if (selectedProducts.isEmpty()) {
                    productError = true
                    onError("Debe seleccionar al menos un producto")
                    return@launch
                }

                val orderRepo = OrdersRepository(api = ApiConnection.api)

                val productRequests = selectedProducts.map {
                    ProductRequest(
                        productId = it.productId.toInt(),
                        quantity = 1
                    )
                }

                val request = CreateOrderRequest(
                    client_id = clientId,
                    products = productRequests,
                    estimated_delivery_time = "2025-10-25T14:00:00",
                    status_id = 3
                )

                val response = orderRepo.createOrder(request)

                onSuccess(response.order_id, response.message)

            } catch (e: Exception) {
                onError("Error al crear orden: ${e.message}")
            }
        }
    }
}
