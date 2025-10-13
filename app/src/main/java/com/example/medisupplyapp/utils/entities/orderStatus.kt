package com.example.medisupplyapp.utils.entities

import androidx.compose.ui.graphics.Color
import com.example.medisupplyapp.R

enum class OrderStatus(val displayName: Int, val color: Color) {
    PENDING_APPROVAL(R.string.pending_approval, Color(0xFFFBBC04)),
    PROCESSING(R.string.processing, Color(0xFF4285F4)),
    IN_TRANSIT(R.string.in_transit, Color(0xFF9C27B0)),
    DELIVERED(R.string.delivered, Color(0xFF0F9D58)),
    CANCELLED(R.string.cancelled, Color(0xFFEA4335)),
    DELAYED(R.string.delayed, Color(0xFFFF9800))
}