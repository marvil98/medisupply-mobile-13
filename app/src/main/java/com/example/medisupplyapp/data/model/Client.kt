package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName

data class Client(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("identification") val identification: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("password") val password: String
) {
    val fullName: String get() = "$name $lastName"
}

data class ClientResponse(
    @SerializedName("users") val users: List<Client>
)
