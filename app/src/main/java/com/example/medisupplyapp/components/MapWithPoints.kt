package com.example.medisupplyapp.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.medisupplyapp.data.model.RoutePoint
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.collections.forEach

@Composable
fun BoxScope.MapWithPoints(visitPoints: List<RoutePoint>) {
    var puntoSeleccionado by remember { mutableStateOf<RoutePoint?>(null) }

    // Calcular el centro del mapa basado en todos los puntos
    val centerLat = visitPoints.map { it.latitude }.average()
    val centerLng = visitPoints.map { it.longitude }.average()

    val camaraPosicion = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(centerLat, centerLng), 12f)
    }

    // Ajustar la cámara para mostrar todos los puntos
    LaunchedEffect(visitPoints) {
        if (visitPoints.size > 1) {
            val boundsBuilder = LatLngBounds.builder()
            visitPoints.forEach { point ->
                boundsBuilder.include(LatLng(point.latitude, point.longitude))
            }
            val bounds = boundsBuilder.build()
            camaraPosicion.move(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }
    }

    // Mapa de Google
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = camaraPosicion,
        properties = MapProperties(
            isMyLocationEnabled = false
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            myLocationButtonEnabled = false
        )
    ) {
        // Dibujar la ruta (polyline) entre todos los puntos
        if (visitPoints.size > 1) {
            val routePoints = visitPoints.map { LatLng(it.latitude, it.longitude) }
            Polyline(
                points = routePoints,
                color = MaterialTheme.colorScheme.primary,
                width = 8f,
                geodesic = true
            )
        }

        // Marcadores en cada punto
        visitPoints.forEachIndexed { index, punto ->
            Marker(
                state = MarkerState(position = LatLng(punto.latitude, punto.longitude)),
                title = "${index + 1}. ${punto.name}",
                snippet = punto.address,
                onClick = { marker ->
                    puntoSeleccionado = punto
                    marker.showInfoWindow()
                    true
                }
            )
        }
    }

    // Panel de información inferior (más bonito que un AlertDialog)
    if (puntoSeleccionado != null) {
        BottomInfoPanel(
            point = puntoSeleccionado!!,
            onDismiss = { puntoSeleccionado = null }
        )
    }
}
