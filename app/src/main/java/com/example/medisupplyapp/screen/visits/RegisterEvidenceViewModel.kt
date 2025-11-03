package com.example.medisupplyapp.screen.visits

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.medisupplyapp.data.model.Recommendation
import com.example.medisupplyapp.data.remote.ApiConnection
import com.example.medisupplyapp.data.remote.repository.ClientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

sealed class UploadState {
    data object Idle : UploadState()
    data object SubiendoEvidencias : UploadState()
    data object ProcesandoVisita : UploadState()
    data class Error(val message: String) : UploadState()
    data object Success : UploadState()
    data class SuccessWithData(val recommendations: List<Recommendation>) : UploadState()
}


class RegisterEvidenceViewModel(application: Application) : AndroidViewModel(application) {

    private val clientRepository = ClientRepository(api = ApiConnection.api_users)

    private val isUploading = AtomicBoolean(false)
    private val _uiState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uiState: StateFlow<UploadState> = _uiState.asStateFlow()

    fun uploadEvidences(
        visitId: Int,
        files: List<File>,
        clientId: Int,
    ) {
        if (isUploading.getAndSet(true)) return

        _uiState.value = UploadState.SubiendoEvidencias

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val filesToUpload = files.map { file ->
                    val mimeType = getMimeType(file.extension)
                    val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())

                    MultipartBody.Part.createFormData(
                        name = "files",
                        filename = file.name,
                        body = requestBody
                    )
                }

                val result = clientRepository.uploadVisitEvidences(visitId, filesToUpload)

                result
                    .onSuccess {
                        files.forEach { it.delete() }

                        requestRecommendations(clientId)
                    }
                    .onFailure { error ->
                        _uiState.value = UploadState.Error(error.message ?: "Fallo al subir evidencias.")
                        isUploading.set(false)
                    }
            } catch (e: Exception) {
                _uiState.value = UploadState.Error("Error inesperado en la subida: ${e.message}")
                isUploading.set(false)
            }
        }
    }

    private fun requestRecommendations(clientId: Int) {
        _uiState.value = UploadState.ProcesandoVisita

        viewModelScope.launch(Dispatchers.IO) {
            val result = clientRepository.getRecommendations(clientId, regionalSetting = "CO")

            result
                .onSuccess { response ->
                    if (response.status == "success") {
                        _uiState.value = UploadState.SuccessWithData(response.recommendations)
                    } else {
                        _uiState.value = UploadState.Error("Respuesta de microservicio inválida.")
                    }
                }
                .onFailure { error ->
                    _uiState.value = UploadState.Error("Fallo al obtener recomendaciones: ${error.message}")
                }

            isUploading.set(false)
        }
    }

    private fun getMimeType(extension: String): String {
        return when (extension.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "mp4" -> "video/mp4"
            "pdf" -> "application/pdf"
            else -> "*/*"
        }
    }
}