package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.model.ProductUpdateRequest
import com.example.medisupplyapp.data.model.UpdateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProductsApi {
    @GET("products/list")
    suspend fun getProducts(): Response<List<Product>>

    @PUT("products/update-stock")
    suspend fun updateStock(
        @Body request: ProductUpdateRequest
    ): Response<UpdateResponse>
}