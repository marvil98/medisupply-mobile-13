package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderStatus
import com.example.medisupplyapp.data.remote.ApiConnection

class OrdersRepository {
    suspend fun getOrders(): Result<List<Order>> {
        return try {
            val response = ApiConnection.api.getOrders(userId = "USER_55")

            if (response.isEmpty()) {
                Result.success(emptyList())
            } else {
                val orders = response.map { orderResponse ->
                    Order(
                        id = orderResponse.id,
                        creationDate = orderResponse.creationDate,
                        estimatedReleaseDate = orderResponse.estimatedReleaseDate,
                        lastUpdate =  orderResponse.lastUpdate,
                        status = mapStatus(orderResponse.status)
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

    private fun mapStatus(status: String): OrderStatus {
        return when(status.lowercase()) {
            "Pendiente de aprobación" -> OrderStatus.PENDING_APPROVAL
            "Procesando" -> OrderStatus.PROCESSING
            "En camino" -> OrderStatus.IN_TRANSIT
            "Entregado" -> OrderStatus.DELIVERED
            "Cancelado" -> OrderStatus.CANCELLED
            "Demorado" -> OrderStatus.DELAYED
            else -> OrderStatus.PROCESSING
        }
    }
}