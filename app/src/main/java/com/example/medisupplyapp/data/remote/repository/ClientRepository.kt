package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Client
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


class ClientRepository(var api: UsersApi) {
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
}