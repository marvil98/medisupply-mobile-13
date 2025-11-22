package com.example.medisupplyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Respuesta para CLIENT
data class ClientInfoResponse(
    @SerializedName("client_info")
    val clientInfo: ClientInfoDto
)

data class ClientDetailResponse(
    @SerializedName("client_id") val clientId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("seller_zone") val sellerZone: String,
    @SerializedName("balance") val balance: String
)


data class ClientInfoDto(
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("seller_id")
    val sellerId: Int
)

// Respuesta para SELLER
data class SellerInfoResponse(
    @SerializedName("seller_info")
    val sellerInfo: SellerInfoDto
)

data class SellerInfoDto(
    @SerializedName("seller_id")
    val sellerId: Int
)