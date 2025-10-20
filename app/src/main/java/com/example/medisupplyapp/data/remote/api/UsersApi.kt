package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.ClientResponse
import retrofit2.Response
import retrofit2.http.GET

interface UsersApi {
    @GET("api/users/clients")
    suspend fun getClients(): Response<ClientResponse>
}