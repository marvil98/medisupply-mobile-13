package com.example.medisupplyapp.utils

import android.content.Context
import android.net.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.content.FileProvider
import com.example.medisupplyapp.data.model.*
import kotlinx.coroutines.delay
import java.io.*

suspend fun copyToCacheWithProgress(
    context: Context,
    evidences: SnapshotStateList<Evidence>,
    evidence: Evidence,
    sourceUri: Uri
): Uri? {
    val index = evidences.indexOfFirst { it.id == evidence.id }
    if (index == -1) return null

    val mimeType = context.contentResolver.getType(sourceUri)
    val extension = when {
        mimeType?.startsWith("image/") == true -> ".jpg"
        mimeType?.startsWith("video/") == true -> ".mp4"
        else -> ".file"
    }

    val tempFile = File(
        context.cacheDir,
        "copy_${System.currentTimeMillis()}$extension"
    )

    evidences[index] = evidence.copy(statusUpdate = StatusUpdate.SUBIENDO, progress = 0f)
    var isCopySuccessful = true
    var cacheUriResult: Uri? = null

    try {
        val inputStream = context.contentResolver.openInputStream(sourceUri) ?: throw IOException("No se pudo abrir el InputStream.")
        val outputStream = FileOutputStream(tempFile)

        val totalBytes = inputStream.available().toLong()
        var bytesCopied: Long = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var read: Int

        inputStream.use { input ->
            outputStream.use { output ->
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    bytesCopied += read

                    if (totalBytes > 0) {
                        val realProgress = bytesCopied.toFloat() / totalBytes.toFloat()
                        val visualProgress = realProgress * 0.8f
                        val currentEvidence = evidences[index]
                        evidences[index] = currentEvidence.copy(progress = visualProgress.coerceAtMost(0.8f))
                    }
                }
            }
        }

        cacheUriResult = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

        var finalProgress = 0.8f
        while (finalProgress < 1.0f) {
            delay(250)
            finalProgress += 0.04f
            val currentEvidence = evidences[index]
            evidences[index] = currentEvidence.copy(progress = finalProgress.coerceAtMost(1f))
        }

        val failureChance = (0..99).random()
        isCopySuccessful = failureChance >= 33

    } catch (e: Exception) {
        e.printStackTrace()
        isCopySuccessful = false
        tempFile.delete()
    }

    val finalEvidence = evidences[index]
    evidences[index] = finalEvidence.copy(
        statusUpdate = if (isCopySuccessful) StatusUpdate.COMPLETADO else StatusUpdate.ERROR,
        progress = 1f
    )

    return if (isCopySuccessful) cacheUriResult else null
}