package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.CreateOrderRequest
import com.example.medisupplyapp.data.model.CreateOrderResponse
import com.example.medisupplyapp.data.model.Item
import com.example.medisupplyapp.data.model.Order
import com.example.medisupplyapp.data.model.OrderDetail
import com.example.medisupplyapp.data.model.OrderStatus
import com.example.medisupplyapp.data.model.toOrderDetail


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



    suspend fun getOrderDetail(orderId: Int): Result<OrderDetail> {
        return try {
            // Llamar a la API que retorna OrderDetailResponse
            val response = api.getOrderDetail(orderId = orderId)

            // Extraer el DTO y convertir al modelo de dominio
            val orderDetailDto = response.order
            val orderDetail = orderDetailDto.toOrderDetail()

            // Validar el modelo convertido
            if (isValidOrderDetail(orderDetail)) {
                Result.success(orderDetail)
            } else {
                Result.failure(IllegalArgumentException("Datos del pedido inválidos"))
            }

        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                404 -> Result.failure(IllegalArgumentException("Pedido no encontrado"))
                401 -> Result.failure(SecurityException("No autorizado"))
                500 -> Result.failure(IllegalStateException("Error del servidor"))
                else -> Result.failure(e)
            }
        } catch (e: java.net.UnknownHostException) {
            Result.failure(IllegalStateException("Sin conexión a internet"))
        } catch (e: java.net.SocketTimeoutException) {
            Result.failure(IllegalStateException("Tiempo de espera agotado"))
        } catch (e: com.google.gson.JsonSyntaxException) {
            Result.failure(IllegalStateException("Error al procesar la respuesta del servidor"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isValidOrderDetail(orderDetail: OrderDetail): Boolean {
        return try {
            // Validar campos requeridos
            orderDetail.id > 0 &&
                    orderDetail.orderValue >= 0 &&
                    orderDetail.clientName.isNotBlank() &&
                    orderDetail.sellerName.isNotBlank() &&
                    orderDetail.items.isNotEmpty() &&
                    // Validar que todos los items tengan datos válidos
                    orderDetail.items.all { item ->
                        item.productId > 0 &&
                                item.name.isNotBlank() &&
                                item.priceUnit >= 0 &&
                                item.quantity > 0 &&
                                item.sku.isNotBlank()
                    }
        } catch (e: Exception) {
            false
        }
    }
}