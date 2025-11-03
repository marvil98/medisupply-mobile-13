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

data class EvidenceDTO(
    val evidence_id: Int,
    val type: String,
    val url: String,
    val visit_id: Int
)

data class UploadEvidenceResponse(
    val evidences: List<EvidenceDTO>,
    val message: String
)

data class RecommendationResponse(
    val recommendations: List<Recommendation>,
    val status: String
)

data class Recommendation(
    val product_name: String?,
    val product_sku: String?,
    val reasoning: String?,
    val score: Double?
)

data class RecommendationRequest(
    val client_id: Int,
    val regional_setting: String
)