package com.example.medisupplyapp.screen.orders

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.CreateOrderRequest
import com.example.medisupplyapp.data.model.OrderState
import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.model.ProductRequest
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import com.example.medisupplyapp.data.remote.repository.OrdersRepository
import com.example.medisupplyapp.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime

class CreateOrderViewModel(application: Application) : AndroidViewModel(application) {
    var selectedClient by mutableStateOf<Client?>(null)
    var clientError by mutableStateOf(false)
    var clients by mutableStateOf<List<Client>>(emptyList())
    var products by mutableStateOf<List<Product>>(emptyList())
    var selectedProducts by mutableStateOf<List<Product>>(emptyList())
    var productError by mutableStateOf(false)
    private val _orderState = MutableLiveData<OrderState>()
    val orderState: LiveData<OrderState> = _orderState

    init {
        viewModelScope.launch {
            try {
                val repo = ClientRepository(api = ApiConnection.api_users)
                val result = repo.fecthClientsBySellerID(1)
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

    fun createOrder(
        selectedQuantities: Map<String, Int>,
        onSuccess: (orderId: String, message: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val clientId = selectedClient?.userId
                if (clientId == null) {
                    clientError = true
                    onError("Cliente inválido")
                    return@launch
                }

                if (selectedQuantities.isEmpty() || selectedQuantities.values.sum() == 0) {
                    productError = true
                    onError("Debe seleccionar al menos un producto")
                    return@launch
                }

                val productRequests = selectedQuantities
                    .filter { it.value > 0 }
                    .map { (productId, quantity) ->
                        ProductRequest(
                            productId = productId,
                            quantity = quantity
                        )
                    }

                val orderRepo = OrdersRepository(api = ApiConnection.api)
                val nowUtc = ZonedDateTime.now(ZoneOffset.UTC)
                val futureUtc = nowUtc.plusDays(5)

                val request = CreateOrderRequest(
                    user_id = clientId.toString(),
                    products = productRequests,
                    estimated_delivery_time = futureUtc.toString(),
                    status_id = 3
                )

                val response = orderRepo.createOrder(request)

                if (response.isSuccess) {
                    val orderResponse = response.getOrNull()
                    if (orderResponse != null) {
                        _orderState.value = OrderState.Success(orderResponse)
                        onSuccess(orderResponse.order_id, "Orden creada con éxito")
                    } else {
                        onError("Respuesta vacía del servidor")
                    }
                } else {
                    val exception = response.exceptionOrNull()
                    _orderState.value = OrderState.Error(exception)
                    onError(exception?.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                onError("Error al crear orden: ${e.message}")
            }
        }
    }
}
