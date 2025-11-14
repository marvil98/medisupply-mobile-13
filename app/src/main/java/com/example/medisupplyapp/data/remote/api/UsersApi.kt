package com.example.medisupplyapp.data.remote.api

import com.example.medisupplyapp.data.model.ClientRecommendationResponse
import com.example.medisupplyapp.data.model.ClientResponse
import com.example.medisupplyapp.data.model.LoginRequest
import com.example.medisupplyapp.data.model.LoginResponse
import com.example.medisupplyapp.data.model.RecommendationRequest
import com.example.medisupplyapp.data.model.RecommendationResponse
import com.example.medisupplyapp.data.model.RegisterVisitRequest
import com.example.medisupplyapp.data.model.RegisterVisitResponse
import com.example.medisupplyapp.data.model.UploadEvidenceResponse
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
}