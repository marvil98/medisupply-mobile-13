package com.example.medisupplyapp.data.model

import android.R
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Client(
    @SerializedName("client_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nit") val nit: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("longitude") val longitude: String,
) {
}

data class ClientResponse(
    @SerializedName("clients") val clients: List<Client>
)

data class RegisterVisitRequest(
    val client_id: Int,
    val seller_id: Int,
    val date: String,
    val findings: String
)

data class RegisterVisitResponse(
    val visit: VisitResponse,
    val message: String
)

data class VisitResponse(
    val client_id: Int,
    val seller_id: Int,
    val visit_id: Int,
    val date: String,
    val findings: String
)