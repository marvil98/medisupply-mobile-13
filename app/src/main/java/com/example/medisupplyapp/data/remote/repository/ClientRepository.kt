package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.model.RegisterVisitResponse
import com.example.medisupplyapp.data.remote.api.UsersApi

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
}