package com.example.medisupplyapp.data.model

import java.util.Date

data class Order(
    val id: String,
    val status: OrderStatus,
    val creationDate: Date,
    val estimatedReleaseDate: Date?,
    val lastUpdate: Date
)