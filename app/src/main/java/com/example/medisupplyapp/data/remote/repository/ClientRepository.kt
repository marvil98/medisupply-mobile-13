package com.example.medisupplyapp.data.remote.repository

import android.content.Context
import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.LoginRequest
import com.example.medisupplyapp.data.model.LoginResponse
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
import com.example.medisupplyapp.datastore.AuthCacheProto
import kotlinx.coroutines.flow.first
import retrofit2.Response
import retrofit2.http.Body


class ClientRepository(var api: UsersApi, private val context: Context) {
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
                    // Guardar datos en Proto DataStore
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
                            .build()
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

    suspend fun logout() {
        context.authCacheDataStore.updateData { currentAuth ->
            AuthCacheProto.getDefaultInstance()
        }
    }

    suspend fun isAuthenticated(): Boolean {
        val authData = context.authCacheDataStore.data.first()
        return authData.accessToken.isNotBlank()
    }

    suspend fun getAccessToken(): String? {
        val authData = context.authCacheDataStore.data.first()
        return if (authData.accessToken.isNotBlank()) authData.accessToken else null
    }

    suspend fun getUserName(): String {
        val authData = context.authCacheDataStore.data.first()
        return "${authData.name} ${authData.lastName}"
    }
}