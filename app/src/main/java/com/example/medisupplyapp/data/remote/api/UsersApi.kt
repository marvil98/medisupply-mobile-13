package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.ClientRecommendationResponse
import com.example.medisupplyapp.data.model.ClientResponse
import com.example.medisupplyapp.data.model.CreateClientRequest
import com.example.medisupplyapp.data.model.CreateClientResponse
import com.example.medisupplyapp.data.model.CreateUserRequest
import com.example.medisupplyapp.data.model.CreateUserResponse
import com.example.medisupplyapp.data.model.LoginRequest
import com.example.medisupplyapp.data.model.LoginResponse
import com.example.medisupplyapp.data.model.LogoutRequest
import com.example.medisupplyapp.data.model.LogoutResponse
import com.example.medisupplyapp.data.model.RecommendationRequest
import com.example.medisupplyapp.data.model.RecommendationResponse
import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.model.RegisterVisitResponse
import com.example.medisupplyapp.data.model.UploadEvidenceResponse
import com.example.medisupplyapp.data.remote.dto.ClientDetailResponse
import com.example.medisupplyapp.data.remote.dto.ClientInfoResponse
import com.example.medisupplyapp.data.remote.dto.SellerInfoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UsersApi {
    @GET("users/clients")
    suspend fun getClients(): Response<ClientResponse>

    @GET("users/clients/{sellerId}")
    suspend fun getClientsBySellerID(@Path("sellerId") sellerId: Int): Response<ClientResponse>

    @POST("users/visit")
    suspend fun registerVisit(@Body visit: RegisterVisitRequest): RegisterVisitResponse

    @Multipart
    @POST("users/visits/{visit_id}/evidences")
    suspend fun uploadEvidences(
        @Path("visit_id") visitId: Int,
        @Part files: List<MultipartBody.Part>
    ): Response<UploadEvidenceResponse>

    @POST("users/recommendations")
    suspend fun getRecommendations(
        @Body requestBody: RecommendationRequest
    ): Response<RecommendationResponse>

    @GET("users/recommendations/client/{clientId}")
    suspend fun getRecommendationsByClient(
        @Path("clientId") clientId: Int
    ): Response<ClientRecommendationResponse>

    @POST("users/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @POST("users/logout")
    suspend fun logout(@Body request: LogoutRequest): Response<LogoutResponse>

    @GET("users/clients/{userId}/info")
    suspend fun getClientInfo(@Path("userId") userId: Int): Response<ClientInfoResponse>

    @GET("users/seller/{userId}/info")
    suspend fun getSellerInfo(@Path("userId") userId: Int): Response<SellerInfoResponse>

    @GET("users/detail/{userId}")
    suspend fun getClientDetail(@Path("userId") userId: Int): Response<ClientDetailResponse>

    @POST("users/create")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>

    @POST("users/clients/create")
    suspend fun createClient(@Body request: CreateClientRequest): Response<CreateClientResponse>
}