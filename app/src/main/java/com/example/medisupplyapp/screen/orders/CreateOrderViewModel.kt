package com.example.medisupplyapp.screen.orders

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CreateOrderViewModel : ViewModel() {
    var selectedClient by mutableStateOf("")
    var selectedProduct by mutableStateOf("")
    var notes by mutableStateOf("")

    var clientError by mutableStateOf(false)
    var productError by mutableStateOf(false)
    var notesError by mutableStateOf(false)

    fun validate(): Boolean {
        clientError = selectedClient.isBlank()
        productError = selectedProduct.isBlank()
        notesError = notes.isBlank()
        return !(clientError || productError || notesError)
    }

    fun createOrder(onSuccess: () -> Unit) {
        if (validate()) {
            onSuccess()
        }
    }

    fun validateClientOnDismiss() {
        if (selectedClient.isBlank()) clientError = true
    }

    fun isFormValid(): Boolean {
        return selectedClient.isNotBlank() &&
                selectedProduct.isNotBlank() &&
                notes.isNotBlank()
    }
}
