package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.CreateOrderRequest
import com.example.medisupplyapp.data.model.CreateOrderResponse
import com.example.medisupplyapp.data.model.OrderDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path // Importa Path
import com.example.medisupplyapp.data.remote.dto.OrderResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OrdersApi {
    // La ruta ahora tiene un placeholder dinámico {userId}
    @GET("orders/track/{userId}")
    // Usamos @Path para inyectar el valor en el placeholder
    suspend fun getOrders(
        @Path("userId") userId: String,
    ): List<OrderResponse>

    @POST("orders/")
    suspend fun createOrder(@Body order: CreateOrderRequest): CreateOrderResponse

    @GET("orders/{orderId}")
    // Usamos @Path para inyectar el valor en el placeholder
    suspend fun getOrderDetail(
        @Path("orderId") orderId: Int,
    ): OrderDetailResponse
}
