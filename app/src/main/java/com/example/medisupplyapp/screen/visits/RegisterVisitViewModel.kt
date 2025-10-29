package com.example.medisupplyapp.screen.visits

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.OrderState

import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class RegisterVisitViewModel : ViewModel() {
    var selectedClient by mutableStateOf<Client?>(null)
    var selectedDate by mutableStateOf<Date?>(null)

    var clientError by mutableStateOf(false)
    var dateError by mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")

    var clients by mutableStateOf<List<Client>>(emptyList())

    var findings: String by mutableStateOf("")


    private val _registerState = MutableLiveData<OrderState>()

    init {
        viewModelScope.launch {
            try {
                val repo = ClientRepository(api = ApiConnection.api_users)
                val result = repo.fecthClientsBySellerID(1)
                clients = result
            } catch (e: Exception) {
                print(e)
            }
        }
    }

    fun validateClientOnDismiss() {
        clientError = selectedClient == null
    }

    fun isFormValid(): Boolean {
        return selectedClient != null && !clientError && selectedDate != null && !dateError
    }

    fun updateFindings(newText: String) {
        findings = newText
    }

    fun onDateSelected(date: Date) {
        val now = Date()
        val calendar = Calendar.getInstance()

        // 1. Validación de Fecha Futura
        // Se resetea la hora de 'now' a 00:00:00 del día actual para una comparación pura de día.
        val todayCalendar = Calendar.getInstance().apply {
            time = now
            set(Calendar.HOUR_OF_DAY, 23) // Establecer a casi final del día para comparación
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        val todayLimit = todayCalendar.time

        if (date.after(todayLimit)) {
            dateError = true
            return
        }

        // 2. Validación de Últimos 30 Días
        calendar.time = now
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        val thirtyDaysAgo = calendar.time

        if (date.before(thirtyDaysAgo)) {
            errorMessage = "La fecha de la visita no puede ser anterior a 30 días."
            dateError = true
            return
        }

        // Si la validación es exitosa
        selectedDate = date
        dateError = false
        errorMessage = ""
    }

    fun registerVisit(
        onSuccess: (visitID: String, message: String) -> Unit,
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
                val clientRepo = ClientRepository(api = ApiConnection.api_users)

                val request = RegisterVisitRequest(
                    client_id = clientId,
                    seller_id = 1,
                    date =selectedDate,
                    findings = findings
                )


                val response = clientRepo.registerVisit(request)

                if (response.isSuccess) {
                    val visitResponse = response.getOrNull()
                    if (visitResponse != null) {
                        onSuccess(visitResponse.visit.visit_id.toString(), "Orden creada con éxito")
                    } else {
                        onError("Respuesta vacía del servidor")
                    }
                } else {
                    val exception = response.exceptionOrNull()
                    _registerState.value = OrderState.Error(exception)
                    onError(exception?.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                onError("Error al crear orden: ${e.message}")
            }
        }
    }
}
