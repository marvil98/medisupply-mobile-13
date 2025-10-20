package com.example.medisupplyapp.data.model

import java.util.Date

data class Order(
    val id: String,
    val status: OrderStatus,
    val creationDate: Date,
    val estimatedReleaseDate: Date?,
    val lastUpdate: Date
)

data class CreateOrderRequest(
    val client_id: Int,
    val products: List<ProductRequest>,
    val estimated_delivery_time: String,
    val status_id: Int
)

data class CreateOrderResponse(
    val order_id: Int,
    val message: String
)