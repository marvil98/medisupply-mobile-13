package com.example.medisupplyapp.data.remote.dto

import com.example.medisupplyapp.data.model.OrderStatus
import java.util.Date

data class OrderResponse(
    val order_id: Int,
    val status: String,
    val creation_date: Date,
    val estimated_delivery_time: Date?,
    val last_updated_date: Date
)