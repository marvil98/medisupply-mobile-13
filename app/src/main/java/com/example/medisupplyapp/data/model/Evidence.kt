package com.example.medisupplyapp.data.model

import android.graphics.Bitmap
import android.net.Uri
import java.time.LocalDateTime
import java.util.UUID

enum class EvidenceType {
    FOTO,
    VIDEO,
    GALERIA
}

enum class StatusUpdate { PENDIENTE, SUBIENDO, COMPLETADO, ERROR }

data class Evidence(
    val id: String = UUID.randomUUID().toString(),
    val type: EvidenceType,
    val nameFile: String,
    val uri: Uri,
    val date: LocalDateTime = LocalDateTime.now(),
    val thumbnail: Bitmap? = null,
    val progress: Float = 0f,
    val statusUpdate: StatusUpdate = StatusUpdate.PENDIENTE
)