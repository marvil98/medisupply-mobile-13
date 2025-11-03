package com.example.medisupplyapp.screen.visits

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.net.*
import android.provider.*
import androidx.activity.compose.*
import androidx.activity.result.contract.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.core.content.FileProvider
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.*
import com.example.medisupplyapp.data.model.*
import com.example.medisupplyapp.utils.*
import com.tuempresa.medisupply.ui.components.*
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.io.*
import kotlinx.coroutines.*


@SuppressLint("UnrememberedMutableState")
@Composable
fun RegisterEvidenceScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    selectedRoute: String,
) {
    var optionsVisibles by remember { mutableStateOf(false) }
    val evidences = remember { mutableStateListOf<Evidence>() }
    val photoUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var toastType by remember { mutableStateOf(ToastType.SUCCESS) }

    val allEvidencesReady by derivedStateOf {
        val hasCompleted = evidences.any { it.statusUpdate == StatusUpdate.COMPLETADO }
        val hasPendingOrUploading = evidences.any {
            it.statusUpdate == StatusUpdate.PENDIENTE || it.statusUpdate == StatusUpdate.SUBIENDO
        }
        hasCompleted && !hasPendingOrUploading
    }

    fun handleEvidenceCopy(tempEvidence: Evidence, originalUri: Uri) {
        evidences.add(tempEvidence)

        scope.launch {
            val cacheUri = copyToCacheWithProgress(context, evidences, tempEvidence, originalUri)

            val successful = cacheUri != null

            showToast = true
            if (successful) {
                toastMessage = context.getString(R.string.success_evidence)
                toastType = ToastType.SUCCESS
            } else {
                toastMessage = context.getString(R.string.error_evidence)
                toastType = ToastType.ERROR
            }

            if (successful) {
                val finalFileName = if (tempEvidence.type == EvidenceType.GALERIA) {
                    tempEvidence.nameFile.replaceAfterLast('.', cacheUri!!.lastPathSegment?.substringAfterLast('.') ?: "jpg")
                } else {
                    cacheUri!!.lastPathSegment ?: tempEvidence.nameFile
                }

                val finalThumbnail = when (tempEvidence.type) {
                    EvidenceType.VIDEO -> generateVideoThumbnail(context, cacheUri!!)
                    else -> generateImageThumbnail(context, cacheUri!!)
                }

                val index = evidences.indexOfFirst { it.id == tempEvidence.id }
                if (index != -1) {
                    evidences[index] = evidences[index].copy(
                        uri = cacheUri!!,
                        nameFile = finalFileName,
                        thumbnail = finalThumbnail
                    )
                }
            }
        }
    }

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri.value != null) {
            val originalUri = photoUri.value!!
            val thumbnail = generateImageThumbnail(context, originalUri)

            val tempEvidence = Evidence(
                type = EvidenceType.FOTO,
                nameFile = originalUri.lastPathSegment ?: "captured_photo_${System.currentTimeMillis()}.jpg",
                uri = originalUri,
                thumbnail = thumbnail,
                statusUpdate = StatusUpdate.PENDIENTE
            )
            handleEvidenceCopy(tempEvidence, originalUri)
        }
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val originalUri = result.data?.data ?: return@rememberLauncherForActivityResult

            val tempEvidence = Evidence(
                type = EvidenceType.VIDEO,
                nameFile = "temp_video_${System.currentTimeMillis()}.mp4",
                uri = originalUri,
                thumbnail = null,
                statusUpdate = StatusUpdate.PENDIENTE
            )
            handleEvidenceCopy(tempEvidence, originalUri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val originalUri = result.data?.data ?: return@rememberLauncherForActivityResult

            val initialFileName = getFileNameFromUri(context, originalUri)
            val initialThumbnail = generateImageThumbnail(context, originalUri)

            val tempEvidence = Evidence(
                type = EvidenceType.GALERIA,
                nameFile = initialFileName,
                uri = originalUri,
                thumbnail = initialThumbnail,
                statusUpdate = StatusUpdate.PENDIENTE
            )
            handleEvidenceCopy(tempEvidence, originalUri)
        }
    }

    fun launchCameraForPhoto() {
        val photoFile = File(
            context.cacheDir,
            "photo_${System.currentTimeMillis()}.jpg"
        )
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
        photoUri.value = uri
        photoLauncher.launch(uri)
    }

    fun launchCameraForVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        videoLauncher.launch(intent)
    }

    fun openGalleryPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    MediSupplyTheme {
        Scaffold(
            topBar = {
                SimpleTopBar(
                    title = stringResource(R.string.visits),
                    onBack = onBack
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val evidencesToSend = evidences.filter { it.statusUpdate == StatusUpdate.COMPLETADO }
                        },
                        enabled = allEvidencesReady,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    ) {
                        Text(stringResource(R.string.finish_evidences_button))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    FooterNavigation(selectedRoute, onNavigate)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        stringResource(R.string.evidences),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .heightIn(min = 300.dp),
                        shape = RoundedCornerShape(10.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = if (evidences.isEmpty()) Alignment.Center else Alignment.TopStart
                        ) {
                            if (evidences.isEmpty()) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.evidences_empty),
                                        color = Color.Black,
                                        style = MaterialTheme.typography.headlineLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    evidences.reversed().forEach { evidencia ->
                                        key(evidencia.id) {
                                            EvidenceCard(
                                                evidence = evidencia,
                                                onRetry = { evidenciaToRetry ->
                                                    val indexToRetry = evidences.indexOfFirst { it.id == evidenciaToRetry.id }
                                                    if (indexToRetry != -1) {
                                                        evidences[indexToRetry] = evidences[indexToRetry].copy(
                                                            statusUpdate = StatusUpdate.PENDIENTE,
                                                            progress = 0f
                                                        )
                                                        scope.launch {
                                                            val cacheUri = copyToCacheWithProgress(
                                                                context,
                                                                evidences,
                                                                evidenciaToRetry,
                                                                evidenciaToRetry.uri
                                                            )

                                                            if (cacheUri != null) {
                                                                val finalThumbnail = when (evidenciaToRetry.type) {
                                                                    EvidenceType.FOTO, EvidenceType.GALERIA -> generateImageThumbnail(context, cacheUri)
                                                                    EvidenceType.VIDEO -> generateVideoThumbnail(context, cacheUri)
                                                                }

                                                                val updatedIndex = evidences.indexOfFirst { it.id == evidenciaToRetry.id }
                                                                if (updatedIndex != -1) {
                                                                    evidences[updatedIndex] = evidences[updatedIndex].copy(
                                                                        uri = cacheUri,
                                                                        thumbnail = finalThumbnail
                                                                    )
                                                                }
                                                            }
                                                        }
                                                    }
                                                },
                                                onDelete = { evidenciaToDelete ->
                                                    scope.launch {
                                                        try {
                                                            val file = File(context.cacheDir, evidenciaToDelete.uri.lastPathSegment ?: "")
                                                            if (file.exists()) {
                                                                file.delete()
                                                            }
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        }
                                                        evidences.remove(evidenciaToDelete)
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { optionsVisibles = !optionsVisibles }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.evidences_option),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = optionsVisibles) {
                        Column {
                            RoundedButton(
                                title = stringResource(R.string.photo_option),
                                navigation = "photo",
                                onOptionClick = { launchCameraForPhoto() },
                                withBorder = true
                            )
                            RoundedButton(
                                title = stringResource(R.string.video_option),
                                navigation = "video",
                                onOptionClick = { launchCameraForVideo() },
                                withBorder = true
                            )
                            RoundedButton(
                                title = stringResource(R.string.gallery_option),
                                navigation = "gallery",
                                onOptionClick = { openGalleryPicker() },
                                withBorder = true
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                FloatingToast(
                    message = toastMessage,
                    type = toastType,
                    visible = showToast,
                    onDismiss = { showToast = false }
                )
            }
        }
    }
}