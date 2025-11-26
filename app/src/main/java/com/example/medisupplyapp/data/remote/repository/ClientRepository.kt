package com.example.medisupplyapp.data.remote.repository

import android.content.Context
import android.util.Log
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.CreateClientRequest
import com.example.medisupplyapp.data.model.CreateClientResponse
import com.example.medisupplyapp.data.model.CreateUserRequest
import com.example.medisupplyapp.data.model.CreateUserResponse
import com.example.medisupplyapp.data.model.LoginRequest
import com.example.medisupplyapp.data.model.LoginResponse
import com.example.medisupplyapp.data.model.LogoutRequest
import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.model.RecommendationResponse
import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.model.RegisterVisitResponse
import com.example.medisupplyapp.data.model.UploadEvidenceResponse
import com.example.medisupplyapp.data.remote.api.UsersApi
import okhttp3.MultipartBody
import com.example.medisupplyapp.data.model.RecommendationRequest
import retrofit2.HttpException
import java.io.IOException
import com.example.medisupplyapp.data.model.toProductList
import com.example.medisupplyapp.data.provider.authCacheDataStore
import com.example.medisupplyapp.data.provider.routeCacheDataStore
import com.example.medisupplyapp.data.remote.dto.ClientDetailResponse
import com.example.medisupplyapp.datastore.AuthCacheProto
import com.example.medisupplyapp.datastore.RouteCacheProto
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit


data class ErrorResponse(
    val errors: List<String>,
    val success: Boolean
)


class ClientRepository(var api: UsersApi, private val context: Context) {

    companion object {
        // Duración de la sesión: 12 horas
        private val SESSION_DURATION_MS = TimeUnit.HOURS.toMillis(1)
    }

    suspend fun fetchClients(): List<Client> {
        val response = api.getClients()
        if (response.isSuccessful) {
            return response.body()?.clients ?: emptyList()
        } else {
            throw Exception("Error al obtener clientes: ${response.code()}")
        }
    }

    suspend fun fecthClientsBySellerID(sellerId: Int):  List<Client>  {
        val response = api.getClientsBySellerID(sellerId)
        if (response.isSuccessful) {
            return response.body()?.clients ?: emptyList()
        } else if(response.code() == 404) {
            return emptyList()
        } else {
            throw Exception("Error al obtener clientes: ${response.code()}")
        }
    }

    suspend fun registerVisit(visit: RegisterVisitRequest): Result<RegisterVisitResponse> {
        return try {
            val response = api.registerVisit(visit)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadVisitEvidences(visitId: Int, files: List<MultipartBody.Part>): Result<UploadEvidenceResponse> {
        return try {
            val response = api.uploadEvidences(visitId, files)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor."))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("Error al subir evidencias: ${response.code()}. Detalle: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendations(
        clientId: Int,
        regionalSetting: String,
        visitId: Int
    ): Result<RecommendationResponse> {
        return try {
            val request = RecommendationRequest(
                client_id = clientId,
                regional_setting = regionalSetting,
                visit_id = visitId
            )

            // Realizar la llamada a la API
            val response = api.getRecommendations(request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta de recomendaciones vacía o mal formada."))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(HttpException(response))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Fallo de red: Verifique su conexión a Internet.", e))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado al obtener recomendaciones.", e))
        }
    }

    suspend fun fetchRecommendProducts(clientId: Int): List<Product> {
        val response = api.getRecommendationsByClient(clientId)

        if (response.isSuccessful) {
            val clientRecommendationResponse = response.body()

            if (clientRecommendationResponse != null) {
                return clientRecommendationResponse.suggestions.toProductList()

            } else {
                return emptyList()
            }

        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Error al obtener recomendaciones: ${response.code()}. Detalles: $errorBody")
        }
    }

    suspend fun createUser(request: CreateUserRequest): CreateUserResponse {
        val response = api.createUser(request)

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                return body
            } else {
                throw Exception("Respuesta vacía del servidor")
            }
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                errorResponse.errors.firstOrNull() ?: "Error desconocido"
            } catch (e: Exception) {
                "Error inesperado: ${e.message}"
            }
            throw Exception(errorMessage)
        }
    }

    suspend fun createClient(request: CreateClientRequest): CreateClientResponse {
        val response = api.createClient(request)

        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        } else {
            val errorBody = response.errorBody()?.string()
            throw Exception("Error al crear cliente: ${response.code()} - $errorBody")
        }
    }

    suspend fun login(email: String, password: String): Result<Boolean> {
        return try {
            val request = LoginRequest(
                correo = email,
                contraseña = password
            )

            val response = api.login(request)

            if (response.isSuccessful) {
                val loginResponse = response.body()

                if (loginResponse != null && loginResponse.success) {
                    // Guardar datos básicos del login + timestamp
                    val currentTimestamp = System.currentTimeMillis()
                    // 1. Guardar datos básicos del login
                    context.authCacheDataStore.updateData { currentAuth ->
                        currentAuth.toBuilder()
                            .setAccessToken(loginResponse.tokens.accessToken)
                            .setName(loginResponse.user.name)
                            .setLastName(loginResponse.user.lastName)
                            .setEmail(loginResponse.user.email)
                            .setUserId(loginResponse.user.userId)
                            .setRole(loginResponse.user.role)
                            .setIdToken(loginResponse.tokens.idToken)
                            .setRefreshToken(loginResponse.tokens.refreshToken)
                            .setLoginTimestamp(currentTimestamp)
                            .build()
                    }

                    // 2. Obtener información adicional según el rol
                    val roleInfoResult = fetchRoleInfo(
                        userId = loginResponse.user.userId,
                        role = loginResponse.user.role
                    )

                    roleInfoResult.onFailure { exception ->
                        Log.w("AUTH", "No se pudo obtener info del rol: ${exception.message}")
                        // No falla el login, solo muestra warning
                    }

                    Result.success(true)
                } else {
                    Result.failure(IllegalArgumentException(
                        loginResponse?.message ?: "Error en el login"
                    ))
                }
            } else {
                when (response.code()) {
                    401 -> Result.failure(SecurityException("Credenciales inválidas"))
                    404 -> Result.failure(IllegalArgumentException("Usuario no encontrado"))
                    500 -> Result.failure(IllegalStateException("Error del servidor"))
                    else -> Result.failure(
                        IllegalStateException("Error HTTP ${response.code()}")
                    )
                }
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(IllegalStateException("Sin conexión a internet"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(IllegalStateException("Tiempo de espera agotado"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Obtener información adicional según el rol
    private suspend fun fetchRoleInfo(userId: Int, role: String): Result<Unit> {
        return try {
            when (role.uppercase()) {
                "CLIENT" -> {
                    val response = api.getClientInfo(userId)
                    if (response.isSuccessful) {
                        val clientInfo = response.body()?.clientInfo
                        if (clientInfo != null) {
                            // Guardar información del cliente
                            context.authCacheDataStore.updateData { currentAuth ->
                                currentAuth.toBuilder()
                                    .setClientId(clientInfo.clientId)
                                    .setSellerId(clientInfo.sellerId)
                                    .build()
                            }
                            Log.d("AUTH", "Info CLIENT guardada: clientId=${clientInfo.clientId}, sellerId=${clientInfo.sellerId}")
                            Result.success(Unit)
                        } else {
                            Result.failure(IllegalStateException("Respuesta vacía"))
                        }
                    } else {
                        Result.failure(IllegalStateException("Error ${response.code()}"))
                    }
                }

                "SELLER" -> {
                    val response = api.getSellerInfo(userId)
                    if (response.isSuccessful) {
                        val sellerInfo = response.body()?.sellerInfo
                        if (sellerInfo != null) {
                            // Guardar información del vendedor
                            context.authCacheDataStore.updateData { currentAuth ->
                                currentAuth.toBuilder()
                                    .setSellerId(sellerInfo.sellerId)
                                    .build()
                            }
                            Log.d("AUTH", "Info SELLER guardada: sellerId=${sellerInfo.sellerId}")
                            Result.success(Unit)
                        } else {
                            Result.failure(IllegalStateException("Respuesta vacía"))
                        }
                    } else {
                        Result.failure(IllegalStateException("Error ${response.code()}"))
                    }
                }

                else -> {
                    Log.w("AUTH", "Rol no reconocido: $role")
                    Result.success(Unit) // No es error crítico
                }
            }
        } catch (e: Exception) {
            Log.e("AUTH", "Error obteniendo info del rol: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Boolean> {
        return try {
            val request = LogoutRequest(
                accessToken = getAccessToken()!!
            )

            val response = api.logout(request)

            if (response.isSuccessful) {
                val logoutResponse = response.body()

                if (logoutResponse != null && logoutResponse.success) {
                    clearCache()
                    Result.success(true)
                } else {
                    Result.failure(IllegalArgumentException(
                        logoutResponse?.message ?: "Error en el login"
                    ))
                }
            } else {
                when (response.code()) {
                    401 -> Result.failure(SecurityException("Credenciales inválidas"))
                    404 -> Result.failure(IllegalArgumentException("Usuario no encontrado"))
                    500 -> Result.failure(IllegalStateException("Error del servidor"))
                    else -> Result.failure(
                        IllegalStateException("Error HTTP ${response.code()}")
                    )
                }
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(IllegalStateException("Sin conexión a internet"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(IllegalStateException("Tiempo de espera agotado"))
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    private suspend fun clearCache() {
        context.authCacheDataStore.updateData {
            AuthCacheProto.getDefaultInstance()
        }
        context.routeCacheDataStore.updateData {
            RouteCacheProto.getDefaultInstance()
        }
    }

    suspend fun getAccessToken(): String? {
        val authData = context.authCacheDataStore.data.first()
        return if (authData.accessToken.isNotBlank()) authData.accessToken else null
    }

    suspend fun getUserName(): String {
        val authData = context.authCacheDataStore.data.first()
        return "${authData.name} ${authData.lastName}"
    }
    suspend fun getUserRole(): String {
        val authData = context.authCacheDataStore.data.first()
        return authData.role
    }

    suspend fun getClientId(): Int? {
        val authData = context.authCacheDataStore.data.first()
        return if (authData.clientId > 0) authData.clientId else null
    }

    suspend fun getSellerId(): Int? {
        val authData = context.authCacheDataStore.data.first()
        return if (authData.sellerId > 0) authData.sellerId else null
    }

    suspend fun fetchClientInfo(userId: Int): ClientDetailResponse? {
        val response = api.getClientDetail(userId)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun isSessionValid(): Boolean {
        val authData = context.authCacheDataStore.data.first()

        // Verificar que tenga token
        if (authData.accessToken.isBlank()) {
            Log.d("AUTH", "No hay sesión activa - Token vacío")
            return false
        }

        // Verificar que no haya expirado (12 horas)
        val currentTime = System.currentTimeMillis()
        val loginTime = authData.loginTimestamp
        val timeSinceLogin = currentTime - loginTime

        val isValid = timeSinceLogin < SESSION_DURATION_MS

        if (isValid) {
            val hoursRemaining = TimeUnit.MILLISECONDS.toHours(SESSION_DURATION_MS - timeSinceLogin)
            Log.d("AUTH", "Sesión válida - Expira en $hoursRemaining horas")
        } else {
            Log.d("AUTH", "Sesión expirada - Han pasado ${TimeUnit.MILLISECONDS.toHours(timeSinceLogin)} horas")
        }

        return isValid
    }
}