package com.example.medisupplyapp.screen.visits

import DailyRouteSerializer
import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.OrderState

import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import com.example.medisupplyapp.data.remote.repository.RoutesRepository
import com.example.medisupplyapp.datastore.RouteCacheProto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date


import com.example.medisupplyapp.data.provider.routeCacheDataStore
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class RegisterVisitViewModel(application: Application) : AndroidViewModel(application) {

    var selectedClient by mutableStateOf<Client?>(null)
    var selectedDate: Long? by mutableStateOf(null)

    var clientError by mutableStateOf(false)
    var dateError by mutableStateOf(false)
    var errorMessage: String by mutableStateOf("")

    var clients by mutableStateOf<List<Client>>(emptyList())

    var findings: String by mutableStateOf("")


    private val _registerState = MutableLiveData<OrderState>()

    init {
        viewModelScope.launch {
            try {
                val repo = ClientRepository(api = ApiConnection.api_users, application)
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
        return selectedClient != null &&
                !clientError && selectedDate != null &&
                !dateError && findings != ""
    }

    fun updateFindings(newText: String) {
        findings = newText
    }

    fun onDateSelected(date: Date) {
        val now = Date()

        val todayLimitCalendar = Calendar.getInstance().apply {
            time = now
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayLimit = todayLimitCalendar.time

        if (date.after(todayLimit) || date == todayLimit) {
            errorMessage = "La fecha de la visita no puede ser posterior a la fecha actual."
            dateError = true
            return
        }

        val thirtyDaysAgoCalendar = Calendar.getInstance().apply {
            time = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, -30)
        }
        val thirtyDaysAgo = thirtyDaysAgoCalendar.time

        if (date.before(thirtyDaysAgo)) {
            errorMessage = "La fecha de la visita no puede ser anterior a 30 días."
            dateError = true
            return
        }

        selectedDate = date.time
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
                val dateMillis = selectedDate

                if (clientId == null || dateMillis == null) {
                    clientError = true
                    onError("Cliente o fecha inválidos")
                    return@launch
                }

                val instant = Instant.ofEpochMilli(dateMillis)
                val utcZone = ZoneId.of("UTC")
                val zonedDateTime = instant
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .atStartOfDay()
                    .plusHours(12)
                    .atZone(utcZone)

                val iso8601Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .withZone(utcZone)

                val formattedDateString = zonedDateTime.format(iso8601Formatter)

                val clientRepo = ClientRepository(api = ApiConnection.api_users, application)
                val routesRepo = RoutesRepository(
                    api = ApiConnection.api_routes,
                    routeCacheDataStore = application.routeCacheDataStore
                )

                val request = RegisterVisitRequest(
                    client_id = clientId,
                    seller_id = 1,
                    date = formattedDateString,
                    findings = findings
                )


                val response = clientRepo.registerVisit(request)

                if (response.isSuccess) {

                    val visitResponse = response.getOrNull()
                    if (visitResponse != null) {
                        val visitsMade = routesRepo.visitsMadeFlow.first()

                        routesRepo.updateVisitsMade(visitsMade+1)
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
