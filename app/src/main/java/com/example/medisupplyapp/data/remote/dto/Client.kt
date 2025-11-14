package com.example.medisupplyapp.data.remote.dto

import com.google.gson.annotations.SerializedName

// Respuesta para CLIENT
data class ClientInfoResponse(
    @SerializedName("client_info")
    val clientInfo: ClientInfoDto
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