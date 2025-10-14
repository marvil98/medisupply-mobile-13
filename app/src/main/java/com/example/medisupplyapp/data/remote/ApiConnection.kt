package com.example.medisupplyapp.data.remote

import com.example.medisupplyapp.data.remote.api.OrdersApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConnection {
    // Hemos confirmado que esta URL es correcta para el emulador
    private const val BASE_URL = "http://10.0.2.2:8080"

    // Cliente OkHttp con logging configurado
    private val okHttpClient = OkHttpClientBuilder.build()

    val api: OrdersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 🚀 CRÍTICO: Añadir el cliente OkHttpClient configurado con el logger
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrdersApi::class.java)
    }
}
