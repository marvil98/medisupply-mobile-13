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
    val user_id: String,
    val products: List<ProductRequest>,
    val estimated_delivery_time: String,
    val status_id: Int
)

data class CreateOrderResponse(
    val order_id: String,
    val message: String
)