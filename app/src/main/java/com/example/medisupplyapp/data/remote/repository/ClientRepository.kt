package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Client
import com.example.medisupplyapp.data.remote.api.UsersApi

class ClientRepository(var api: UsersApi) {
    suspend fun fetchClients(): List<Client> {
        val response = api.getClients()
        if (response.isSuccessful) {
            return response.body()?.users ?: emptyList()
        } else {
            throw Exception("Error al obtener clientes: ${response.code()}")
        }
    }
}