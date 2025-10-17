package com.example.medisupplyapp.data.remote.dto

import com.example.medisupplyapp.data.model.OrderStatus
import java.util.Date

data class OrderResponse(
    val numero_pedido: String,
    val estado_nombre: String,
    val fecha_creacion: Date,
    val fecha_entrega_estimada: Date,
    val fecha_ultima_actualizacion: Date
)