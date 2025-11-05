package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.CreateOrderRequest
import com.example.medisupplyapp.data.model.CreateOrderResponse
import com.example.medisupplyapp.data.model.Item
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderDetail
import com.example.medisupplyapp.data.model.OrderStatus


import com.example.medisupplyapp.data.remote.api.OrdersApi
import com.google.protobuf.copy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersRepository(var api: OrdersApi) {
    suspend fun getOrders(userID: String): Result<List<Order>> {
        return try {
            val response = api.getOrders(userId = userID)

            if (response.isEmpty()) {
                Result.success(emptyList())
            } else {
                val orders = response.map { orderResponse ->
                    Order(
                        id = orderResponse.order_id,
                        creationDate = orderResponse.creation_date,
                        estimatedReleaseDate = orderResponse.estimated_delivery_time,
                        lastUpdate =  orderResponse.last_updated_date,
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

    suspend fun createOrder(order: CreateOrderRequest): Result<CreateOrderResponse> {
        return try {
            val response = api.createOrder(order)
            Result.success(response)
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


    fun createDate(dateString: String): Date {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString) ?: Date()
    }


    val mockOrderDetail = OrderDetail(
        id = 1,
        status = mapStatus("3"),
        creationDate = createDate("2025-10-15"),
        lastUpdate = createDate("2025-10-15"),
        client = "Farmacia la especial",
        orderValue = 899.9.toFloat(),
        seller = "Juan Pérez",
        address = "Cra 69i # 71 - 55",
        estimatedReleaseDate=  createDate("2025-10-20"),
        items = listOf(
            Item(
                productId = 1,
                name = "Acetaminofén 500mg",
                priceUnit = 8.5.toFloat(),
                quantity = 100,
                sku = "MED-001"
            ),
            Item(
                productId = 4,
                name = "Guantes Nitrilo Talla M",
                priceUnit = 4.99.toFloat(),
                quantity = 10,
                sku = "SURG-002"
            ),
        )
    )

    suspend fun getOrderDetail(orderId: Int): Result<OrderDetail> {
        return try {
            // Simular delay de red (opcional)
            kotlinx.coroutines.delay(500)

            // Retornar el mock
            Result.success(mockOrderDetail) // Usa el orderId recibido

            /* Cuando el backend esté listo, descomenta esto:
            val response = api.getOrderDetail(orderId = orderId)

            if (response.isSuccessful) {
                val orderDetail = response.body()
                if (orderDetail != null && isValidOrderDetail(orderDetail)) {
                    Result.success(orderDetail)
                } else {
                    Result.failure(IllegalArgumentException("Datos del pedido inválidos"))
                }
            } else {
                when (response.code()) {
                    404 -> Result.failure(IllegalArgumentException("Pedido no encontrado"))
                    else -> Result.failure(IllegalStateException("Error HTTP ${response.code()}"))
                }
            }
            */
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}