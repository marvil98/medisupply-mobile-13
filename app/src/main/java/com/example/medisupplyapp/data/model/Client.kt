package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    @SerializedName("client_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("nit") val nit: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("longitude") val longitude: String,
): Parcelable

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
data class LoginRequest(
    @SerializedName("correo")
    val correo: String,
    @SerializedName("contraseña")
    val contraseña: String
)

data class LoginResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("tokens")
    val tokens: TokensDto,
    @SerializedName("user")
    val user: UserDto
)

data class LogoutRequest(
    @SerializedName("access_token")
    val accessToken: String
)

data class LogoutResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)



data class TokensDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("id_token")
    val idToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("token_type")
    val tokenType: String
)

data class UserDto(
    @SerializedName("email")
    val email: String,
    @SerializedName("identification")
    val identification: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("role")
    val role: String,
    @SerializedName("user_id")
    val userId: Int
)