package com.example.medisupplyapp.screen.visits

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import java.util.Date

class RegisterVisitViewModel : ViewModel() {
    var selectedClient by mutableStateOf<Client?>(null)
    var selectedDate by mutableStateOf<Date?>(Date())

    var clientError by mutableStateOf(false)
    var dateError by mutableStateOf(false)
    var clients by mutableStateOf<List<Client>>(emptyList())

    private val _registerState = MutableLiveData<OrderState>()
    val registerState: LiveData<OrderState> = _registerState

    init {
        viewModelScope.launch {
            try {
                val repo = ClientRepository(api = ApiConnection.api_users)
                val result = repo.fetchClients()
                clients = result
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


    fun onDateSelected(date: Date) {
        selectedDate = date
        dateError = false
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


                val orderRepo = OrdersRepository(api = ApiConnection.api)
                val nowUtc = ZonedDateTime.now(ZoneOffset.UTC)
                val futureUtc = nowUtc.plusDays(5)


/***
                val response = orderRepo.createOrder(request)

                if (response.isSuccess) {
                    val orderResponse = response.getOrNull()
                    if (orderResponse != null) {
                        _registerState.value = OrderState.Success(orderResponse)
                        onSuccess(orderResponse.order_id, "Orden creada con éxito")
                    } else {
                        onError("Respuesta vacía del servidor")
                    }
                } else {
                    val exception = response.exceptionOrNull()
                    _registerState.value = OrderState.Error(exception)
                    onError(exception?.message ?: "Error desconocido")
                }***/
            } catch (e: Exception) {
                onError("Error al crear orden: ${e.message}")
            }
        }
    }
}
