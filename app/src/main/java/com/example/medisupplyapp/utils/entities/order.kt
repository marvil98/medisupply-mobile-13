package com.example.medisupplyapp.utils.entities

import java.util.Date

data class Order(
    val id: String,
    val status: OrderStatus,
    val creation_date: Date,
    val estimated_release_date: Date,
    val last_update: Date
)