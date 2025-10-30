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
    @SerializedName("address") val address: String,
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitud: Double,
    @SerializedName("name") val name: String,
    @SerializedName("client") val client: String,
    @SerializedName("longitude") val longitud: Double,

    ){}