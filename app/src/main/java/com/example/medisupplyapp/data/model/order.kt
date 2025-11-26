package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Order(
    val id: Int,
    val status: OrderStatus,
    val creationDate: Date,
    val estimatedReleaseDate: Date?,
    val lastUpdate: Date
)

data class CreateOrderRequest(
    val client_id: Int,
    val seller_id: Int,
    val status_id: Int,
    val estimated_delivery_time: String,
    val products: List<ProductRequest>
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
    val clientId: Int,
    val clientName: String,
    val orderValue: Float,
    val address: String,
    val sellerId: Int,
    val sellerName: String,
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

data class OrderDetailResponse(
    @SerializedName("order")
    val order: OrderDetailDto
)

data class OrderDetailDto(
    @SerializedName("order_id")
    val orderId: Int,

    @SerializedName("status_id")
    val statusId: Int,

    @SerializedName("creation_date")
    val creationDate: String,

    @SerializedName("last_updated_date")
    val lastUpdatedDate: String,

    @SerializedName("client_id")
    val clientId: Int,

    @SerializedName("client_name")
    val clientName: String,

    @SerializedName("order_value")
    val orderValue: Float,

    @SerializedName("seller_id")
    val sellerId: Int,

    @SerializedName("seller_name")
    val sellerName: String,

    @SerializedName("items")
    val items: List<ItemDto>,

    @SerializedName("estimated_delivery_date")
    val estimatedDeliveryDate: String?,

    @SerializedName("address")
    val address: String
)

data class ItemDto(
    @SerializedName("product_id")
    val productId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("sku")
    val sku: String,

    @SerializedName("price_unit")
    val priceUnit: Float
)

// ============================================
// FUNCIONES DE MAPEO (DTO -> Domain Model)
// ============================================

fun OrderDetailDto.toOrderDetail(): OrderDetail {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    return OrderDetail(
        id = orderId,
        status = mapStatus(statusId),
        creationDate = parseDate(creationDate, dateFormat),
        lastUpdate = parseDate(lastUpdatedDate, dateFormat),
        clientId = clientId,
        clientName = clientName,
        orderValue = orderValue,
        sellerId = sellerId,
        sellerName = sellerName,
        items = items.map { it.toItem() },
        estimatedReleaseDate = estimatedDeliveryDate?.let { parseDate(it, dateFormat) },
        address = address
    )
}

fun ItemDto.toItem(): Item {
    return Item(
        productId = productId,
        name = name,
        quantity = quantity,
        sku = sku,
        priceUnit = priceUnit
    )
}

// Función helper para parsear fechas con manejo de errores
private fun parseDate(dateString: String, dateFormat: SimpleDateFormat): Date {
    return try {
        dateFormat.parse(dateString) ?: Date()
    } catch (e: Exception) {
        Date() // Retorna fecha actual si hay error
    }
}

fun mapStatus(status: Int): OrderStatus {
    return when(status) {
        6 -> OrderStatus.PENDING_APPROVAL
        5 -> OrderStatus.PROCESSING
        1 -> OrderStatus.IN_TRANSIT
        3 -> OrderStatus.DELIVERED
        4 -> OrderStatus.CANCELLED
        2 -> OrderStatus.DELAYED
        else -> OrderStatus.PROCESSING
    }
}