package com.example.medisupplyapp.screen.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isFormValid: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ClientRepository(
        api = ApiConnection.api_users,
        context = application
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                generalError = null
            )
        }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                generalError = null
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        val isEmailValid = email.isNotBlank() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length >= 6

        _uiState.update {
            it.copy(isFormValid = isEmailValid && isPasswordValid)
        }
    }

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        // Validar email
        if (email.isBlank()) {
            _uiState.update { it.copy(emailError = "El correo es requerido") }
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Correo inválido") }
            return
        }

        // Validar contraseña
        if (password.isBlank()) {
            _uiState.update { it.copy(passwordError = "La contraseña es requerida") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(passwordError = "Mínimo 6 caracteres") }
            return
        }

        // Iniciar login
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }

            try {
                val result = repository.login(email, password)

                result.onSuccess { _ ->
                    Log.d("LOGIN", "Login exitoso")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        )
                    }
                }.onFailure { exception ->
                    Log.e("LOGIN", "Error en login: ${exception.message}", exception)
                    val errorMessage = when (exception) {
                        is SecurityException -> "Credenciales inválidas"
                        is IllegalArgumentException -> exception.message ?: "Usuario no encontrado"
                        is IllegalStateException -> exception.message ?: "Error del servidor"
                        else -> "Error desconocido"
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = errorMessage
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "Excepción no manejada: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Error inesperado"
                    )
                }
            }
        }
    }
}