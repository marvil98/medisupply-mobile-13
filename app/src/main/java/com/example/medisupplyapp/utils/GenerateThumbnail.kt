package com.example.medisupplyapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

fun generateImageThumbnail(context: Context, uri: Uri, width: Int = 200, height: Int = 200): Bitmap? {
    return try {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        Bitmap.createScaledBitmap(bitmap, width, height, true)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun generateVideoThumbnail(context: Context, uri: Uri, width: Int = 200, height: Int = 200): Bitmap? {
    return try {
        val retriever = android.media.MediaMetadataRetriever()
        retriever.setDataSource(context, uri)
        val bitmap = retriever.frameAtTime
        retriever.release()
        bitmap?.let { Bitmap.createScaledBitmap(it, width, height, false) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName: String? = null
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
    }
    return fileName ?: "selected_media_${System.currentTimeMillis()}.file"
}

fun copyUriToCache(context: Context, sourceUri: Uri, mimeType: String? = null): Uri? {
    val extension = when {
        mimeType?.startsWith("image/") == true -> ".jpg"
        mimeType?.startsWith("video/") == true -> ".mp4"
        else -> ".file"
    }

    val tempFile = File(
        context.cacheDir,
        "copy_${System.currentTimeMillis()}$extension"
    )

    return try {
        context.contentResolver.openInputStream(sourceUri)?.use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}