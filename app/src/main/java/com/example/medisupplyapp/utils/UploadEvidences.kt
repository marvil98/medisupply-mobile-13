package com.example.medisupplyapp.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.medisupplyapp.data.model.Evidence
import com.example.medisupplyapp.data.model.StatusUpdate
import kotlinx.coroutines.delay

suspend fun simulateUpload(
    evidences: SnapshotStateList<Evidence>,
    evidence: Evidence
) {
    val index = evidences.indexOfFirst { it.id == evidence.id }
    if (index == -1) return

    evidences[index] = evidences[index].copy(
        statusUpdate = StatusUpdate.SUBIENDO,
        progress = 0f
    )

    var progress = 0f

    while (progress < 1f) {
        delay(200)
        progress += 0.1f
        val currentEvidence = evidences[index]
        evidences[index] = currentEvidence.copy(
            statusUpdate = StatusUpdate.SUBIENDO,
            progress = progress
        )
    }

    val success = listOf(true, false, true).random()
    val finalEvidence = evidences[index]
    evidences[index] = finalEvidence.copy(
        statusUpdate = if (success) StatusUpdate.COMPLETADO else StatusUpdate.ERROR,
        progress = 1f
    )
}