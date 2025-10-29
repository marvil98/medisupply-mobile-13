package com.example.medisupplyapp.screen.visits

import android.app.Activity
import android.content.*
import android.net.*
import android.provider.*
import androidx.activity.compose.*
import androidx.activity.result.contract.*
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import com.example.medisupplyapp.R
import com.example.medisupplyapp.components.*
import com.tuempresa.medisupply.ui.components.*
import com.tuempresa.medisupply.ui.theme.MediSupplyTheme
import java.io.*
import androidx.compose.foundation.shape.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.core.content.FileProvider
import com.example.medisupplyapp.data.model.Evidence
import com.example.medisupplyapp.data.model.EvidenceType
import com.example.medisupplyapp.data.model.StatusUpdate
import com.example.medisupplyapp.utils.generateImageThumbnail
import com.example.medisupplyapp.utils.generateVideoThumbnail
import com.example.medisupplyapp.utils.getFileNameFromUri
import com.example.medisupplyapp.utils.simulateUpload
import kotlinx.coroutines.*

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

    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri.value != null) {
            val thumbnail = generateImageThumbnail(context, photoUri.value!!)
            val uri = photoUri.value!!
            val fileName = uri.lastPathSegment ?: "captured_photo_${System.currentTimeMillis()}.jpg"

            val evidencia = Evidence(
                type = EvidenceType.FOTO,
                nameFile = fileName,
                uri = uri,
                thumbnail = thumbnail
            )
            evidences.add(evidencia)
            scope.launch { simulateUpload(evidences, evidencia) }
        }
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@rememberLauncherForActivityResult
            val thumbnail = generateVideoThumbnail(context, uri)
            val timestamp = System.currentTimeMillis()
            val fileName = "captured_video_${timestamp}.mp4"

            val evidencia = Evidence(
                type = EvidenceType.VIDEO,
                nameFile = fileName,
                uri = uri,
                thumbnail = thumbnail
            )
            evidences.add(evidencia)
            scope.launch { simulateUpload(evidences, evidencia) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data ?: return@rememberLauncherForActivityResult
            val thumbnail = generateImageThumbnail(context, uri)
            val fileName = getFileNameFromUri(context, uri)

            val evidencia = Evidence(
                type = EvidenceType.GALERIA,
                nameFile = fileName,
                uri = uri,
                thumbnail = thumbnail
            )
            evidences.add(evidencia)
            scope.launch { simulateUpload(evidences, evidencia) }
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
                FooterNavigation(selectedRoute, onNavigate)
            },
            containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
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
                        .heightIn(min = 300.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
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
                            Column {
                                evidences.forEach { evidencia ->
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
                                                    scope.launch { simulateUpload(evidences, evidenciaToRetry) }
                                                }
                                            },
                                            onDelete = { evidenciaToDelete ->
                                                evidences.remove(evidenciaToDelete)
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
            }
        }
    }
}
