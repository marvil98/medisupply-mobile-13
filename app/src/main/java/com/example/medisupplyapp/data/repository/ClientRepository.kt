package com.example.medisupplyapp.data.repository
import com.example.medisupplyapp.data.model.Client

class ClientRepository {
    suspend fun fetchClients(): List<Client> {
        val response = RetrofitInstance.api2.getClients()
        if (response.isSuccessful) {
            return response.body()?.users ?: emptyList()
        } else {
            throw Exception("Error al obtener clientes: ${response.code()}")
        }
    }
}
