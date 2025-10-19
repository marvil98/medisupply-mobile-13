package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus


import com.example.medisupplyapp.data.remote.api.OrdersApi

class OrdersRepository(var api: OrdersApi) {
    suspend fun getOrders(userID: String): Result<List<Order>> {
        return try {
            val response = api.getOrders(userId = userID)

            if (response.isEmpty()) {
                Result.success(emptyList())
            } else {
                val orders = response.map { orderResponse ->
                    Order(
                        id = orderResponse.numero_pedido,
                        creationDate = orderResponse.fecha_creacion,
                        estimatedReleaseDate = orderResponse.fecha_entrega_estimada,
                        lastUpdate =  orderResponse.fecha_ultima_actualizacion,
                        status = mapStatus(orderResponse.estado_nombre)
                    )
                }
                Result.success(orders)
            }
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                Result.success(emptyList()) // Retorna lista vacía para 404
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun mapStatus(status: String): OrderStatus {
        return when(status.lowercase()) {
            "pendiente de aprobación" -> OrderStatus.PENDING_APPROVAL
            "procesando" -> OrderStatus.PROCESSING
            "en camino" -> OrderStatus.IN_TRANSIT
            "entregado" -> OrderStatus.DELIVERED
            "cancelado" -> OrderStatus.CANCELLED
            "demorado" -> OrderStatus.DELAYED
            else -> OrderStatus.PROCESSING
        }
    }
}