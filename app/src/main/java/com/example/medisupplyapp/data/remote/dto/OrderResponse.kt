package com.example.medisupplyapp.data.remote.dto

import com.example.medisupplyapp.data.model.OrderStatus
import java.util.Date

data class OrderResponse(
    val id: String,
    val status: String,
    val creationDate: Date,
    val estimatedReleaseDate: Date,
    val lastUpdate: Date
)