package com.example.medisupplyapp.screen.auth

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.medisupplyapp.*
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.CreateClientRequest
import com.example.medisupplyapp.data.model.CreateUserRequest
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.launch

data class RegisterUiState(
    val name: String = "",
    val lastname: String = "",
    val password: String = "",
    val identification: String = "",
    val phone: String = "",
    val email: String = "",
    val nit: String = "",
    val companyName: String = "",
    val errors: Map<String, String> = emptyMap()
)

class RegisterViewModel(application: Application)  : AndroidViewModel(application) {
    private val _latitude = mutableStateOf(0.0)
    val latitude: State<Double> = _latitude

    private val _longitude = mutableStateOf(0.0)
    val longitude: State<Double> = _longitude

    private val _address = mutableStateOf("")
    val address: State<String> = _address

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val userRepository = ClientRepository(
        api = ApiConnection.api_users,
        application
    )

    fun updateAddress(newAddress: String, lat: Double, lng: Double) {
        _address.value = newAddress
        _latitude.value = lat
        _longitude.value = lng
    }

    fun updateField(field: String, value: String) {
        val current = _uiState.value
        val updated = when (field) {
            "name" -> current.copy(name = value)
            "lastname" -> current.copy(lastname = value)
            "password" -> current.copy(password = value)
            "identification" -> current.copy(identification = value)
            "phone" -> current.copy(phone = value)
            "email" -> current.copy(email = value)
            "nit" -> current.copy(nit = value)
            "companyName" -> current.copy(companyName = value)
            else -> current
        }
        _uiState.value = updated
    }


    fun validateForm(context: Context): Boolean {
        val errors = mutableMapOf<String, String>()

        if (_uiState.value.name.isBlank()) errors["name"] = context.getString(R.string.required_field)
        if (_uiState.value.lastname.isBlank()) errors["lastname"] = context.getString(R.string.required_field)
        if (_uiState.value.identification.isBlank()) errors["identification"] = context.getString(R.string.required_field)
        if (_uiState.value.email.isBlank()) errors["email"] = context.getString(R.string.required_field)
        if (_uiState.value.phone.isBlank()) errors["phone"] = context.getString(R.string.required_field)
        if (_uiState.value.password.isBlank()) errors["password"] = context.getString(R.string.required_field)
        if (_uiState.value.identification.isBlank()) errors["nit"] = context.getString(R.string.required_field)
        if (_uiState.value.companyName.isBlank()) errors["companyName"] = context.getString(R.string.required_field)
        if (_address.value.isBlank()) errors["address"] = context.getString(R.string.required_field)
        if (!_uiState.value.phone.matches(Regex("^\\+?\\d{7,13}$"))) errors["phone"] = context.getString(R.string.phone_invalid)
        if (!_uiState.value.email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) errors["email"] = context.getString(R.string.email_invalid)
        val password = _uiState.value.password
        val passwordRegex = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$")
        if (!passwordRegex.matches(password)) {
            errors["password"] = context.getString(R.string.psswd_invalid)
        }
        _uiState.value = _uiState.value.copy(errors = errors)
        return errors.isEmpty()
    }

    fun createUserAndCompany(
        context: Context,
        address: String,
        latitude: Double,
        longitude: Double,
        onSuccess: (Int, String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validateForm(context)) {
            onError("Formulario Inválido")
            return
        }

        viewModelScope.launch {
            try {
                val userRequest = CreateUserRequest(
                    name = _uiState.value.name,
                    lastname = _uiState.value.lastname,
                    identification = _uiState.value.identification,
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                    phone = _uiState.value.phone,
                    active = true,
                    role = "CLIENT"
                )

                val userResponse = userRepository.createUser(userRequest)

                if (userResponse.success) {
                   val userId = userResponse.user_id

                    val companyRequest = CreateClientRequest(
                        user_id = userId,
                        nit = _uiState.value.nit,
                        name = _uiState.value.companyName,
                        address = address,
                        latitude = latitude,
                        longitude = longitude
                    )

                    val companyResponse = userRepository.createClient(companyRequest)

                    if (companyResponse.success) {
                        onSuccess(userId, "Error al crear usuario")
                    } else {
                        onError("Error al crear usuario")
                    }
                } else {
                    onError("Error al crear usuario: ${userResponse.warnings.joinToString()}")
                }
            } catch (e: Exception) {
                onError("Error en la creación: ${e.message}")
            }
        }
    }

}
