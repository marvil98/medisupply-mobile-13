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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime

sealed class RecommendationUiState {
    object Loading : RecommendationUiState()
    data class Success(val recommendedProducts: List<Product>) : RecommendationUiState()
    data class Error(val message: String) : RecommendationUiState()
}


class CreateOrderViewModel(application: Application) : AndroidViewModel(application) {
    private val clientRepository = ClientRepository(api = ApiConnection.api_users, application)
    private val productRepository = ProductRepository(api = ApiConnection.api_products)
    private val ordersRepository = OrdersRepository(api = ApiConnection.api)
    private val userRepository = ClientRepository(
        api = ApiConnection.api_users,
        application
    )

    // ESTADOS
    var selectedClient by mutableStateOf<Client?>(null)
    var clientError by mutableStateOf(false)
    var clients by mutableStateOf<List<Client>>(emptyList())
    var products by mutableStateOf<List<Product>>(emptyList())
    var selectedProducts by mutableStateOf<List<Product>>(emptyList())
    var productError by mutableStateOf(false)
    private val _orderState = MutableLiveData<OrderState>()
    val orderState: LiveData<OrderState> = _orderState

    private val _uiState = MutableStateFlow<RecommendationUiState>(RecommendationUiState.Loading)
    val uiState: StateFlow<RecommendationUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val sellerId = userRepository.getSellerId()
                val result = clientRepository.fecthClientsBySellerID(sellerId!!)
                clients = result
            } catch (e: Exception) {
            }

            try {
                products = productRepository.fetchProducts()
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
        selectedQuantities: Map<Int, Int>,
        clientId: Int? = null,
        onSuccess: (orderId: String, message: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val finalClientId = clientId ?: selectedClient?.userId
                val sellerId = userRepository.getSellerId()

                if (finalClientId == null) {
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
                        val product = products.first { it.productId == productId }
                        ProductRequest(
                            product_id = product.productId,
                            name = product.name,
                            sku = product.sku,
                            category_name = product.categoryName,
                            total_quantity = product.totalQuantity,
                            value = product.value,
                            image_url = null,
                            quantity = quantity,
                            price_unit = product.value
                        )
                    }

                val nowUtc = ZonedDateTime.now(ZoneOffset.UTC)
                val futureUtc = nowUtc.plusDays(5)

                val request = CreateOrderRequest(
                    client_id = finalClientId,
                    seller_id = sellerId!!,
                    products = productRequests,
                    estimated_delivery_time = futureUtc.toString(),
                    status_id = 3
                )

                val response = ordersRepository.createOrder(request)

                if (response.isSuccess) {
                    val orderResponse = response.getOrNull()
                    if (orderResponse != null) {
                        _orderState.value = OrderState.Success(orderResponse)

                        try {
                            for ((productId, quantity) in selectedQuantities) {
                                productRepository.updateProductStock(productId, quantity)
                            }
                            onSuccess(orderResponse.order_id, "Orden creada y stock actualizado con éxito")
                        } catch (e: Exception) {
                            onError("Orden creada pero error al actualizar stock: ${e.message}")
                        }
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


    fun loadRecommendations(clientId: Int) {
        _uiState.value = RecommendationUiState.Loading

        viewModelScope.launch {
            try {
                val products = clientRepository.fetchRecommendProducts(clientId)

                _uiState.value = RecommendationUiState.Success(recommendedProducts=products)
            } catch (e: Exception) {
                _uiState.value = RecommendationUiState.Error("Fallo al cargar las recomendaciones: ${e.localizedMessage}")
            }
        }
    }
}