package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.ClientResponse
import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.model.RegisterVisitResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UsersApi {
    @GET("users/clients")
    suspend fun getClients(): Response<ClientResponse>

    @GET("users/clients/{sellerId}")
    suspend fun getClientsBySellerID(@Path("sellerId") sellerId: Int): Response<ClientResponse>

    @POST("/offers/visit")
    suspend fun registerVisit(@Body visit: RegisterVisitRequest): RegisterVisitResponse
}