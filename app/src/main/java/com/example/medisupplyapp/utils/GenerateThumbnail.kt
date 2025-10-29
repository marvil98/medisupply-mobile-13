package com.example.medisupplyapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.OpenableColumns

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