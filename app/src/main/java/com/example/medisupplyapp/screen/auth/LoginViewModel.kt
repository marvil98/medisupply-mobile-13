package com.example.medisupplyapp.screen.auth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

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

class LoginViewModel : ViewModel() {
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

        val isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
                // TODO: Implementar llamada al backend
                // TODO: Guardar datos en cache (id_user)
                // val result = authRepository.login(email, password)

                // Simulación de llamada API
                delay(1500)

                // Simulación de respuesta exitosa
                val success = true // Cambiar según respuesta del backend

                if (success) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = "Usuario no registrado"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Credenciales inválidas"
                    )
                }
            }
        }
    }
}

