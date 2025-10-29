package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DailyRoute(
    @SerializedName("number_visits") val numberVisits: Int,
    @SerializedName("visits") val visits: List<RoutePoint>,
) {
}

@Serializable
data class RoutePoint(
    @SerializedName("direccion") val address: String,
    @SerializedName("id") val id: Int,
    @SerializedName("latitud") val latitud: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("longitud") val longitud: String,

){}