package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi {
    @GET("products/available")
    suspend fun getProducts(): Response<List<Product>>
}