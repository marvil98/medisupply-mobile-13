package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DailyRoute(
    @SerializedName("number_visits") val numberVisits: Int,
    @SerializedName("visits_made") val visitsMade: Int = 0,
    @SerializedName("visits") val visits: List<RoutePoint>,
) {
}

@Serializable
data class RoutePoint(
    @SerializedName("address") val address: String,
    @SerializedName("id") val id: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("client") val client: String,
    @SerializedName("longitude") val longitude: Double,

    ){}