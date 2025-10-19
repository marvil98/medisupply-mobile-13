package com.example.medisupplyapp.screen.orders

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.repository.ClientRepository
import com.example.medisupplyapp.data.repository.ProductRepository
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
                val repo = ClientRepository()
                val result = repo.fetchClients()
                clients = result
            } catch (e: Exception) {
            }

            try {
                val productRepo = ProductRepository()
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

    fun createOrder(onSuccess: () -> Unit) {
        onSuccess()
    }
}
