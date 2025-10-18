package com.example.medisupplyapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path // Importa Path
import com.example.medisupplyapp.data.remote.dto.OrderResponse

interface OrdersApi {
    // La ruta ahora tiene un placeholder dinámico {userId}
    @GET("orders/track/{userId}")
    // Usamos @Path para inyectar el valor en el placeholder
    suspend fun getOrders(
        @Path("userId") userId: String,
    ): List<OrderResponse>
}
