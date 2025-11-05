package com.example.medisupplyapp.data.model

import java.util.Date

data class Order(
    val id: Int,
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

class OrderDetail(
    val id: Int,
    val status: OrderStatus,
    val creationDate: Date,
    val lastUpdate: Date,
    val client: String,
    val orderValue: Float,
    val address: String,
    val seller: String,
    val items: List<Item>,
    val estimatedReleaseDate: Date?,
)

class Item(
    val productId: Int,
    val name: String,
    val quantity: Int,
    val sku: String,
    val priceUnit: Float
)