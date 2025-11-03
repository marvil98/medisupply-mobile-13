package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.DailyRoute
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RoutesApi {
    @GET("routes/seller/{sellerId}")
    suspend fun getSellerDailyRoute(@Path("sellerId") sellerId: Int): Response<DailyRoute>
}