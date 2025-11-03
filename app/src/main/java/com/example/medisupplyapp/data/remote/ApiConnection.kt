package com.example.medisupplyapp.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.medisupplyapp.data.remote.api.OrdersApi
import com.example.medisupplyapp.data.remote.api.ProductsApi
import com.example.medisupplyapp.data.remote.api.RoutesApi
import com.example.medisupplyapp.data.remote.api.UsersApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConnection {
    // Hemos confirmado que esta URL es correcta para el emulador
    private const val BASE_URL = "http://MediSu-MediS-5XPY2MhrDivI-109634141.us-east-1.elb.amazonaws.com"

    // Cliente OkHttp con logging configurado
    private val okHttpClient = OkHttpClientBuilder.build()

    private val customGson: Gson = GsonBuilder()
        // Indica el formato exacto que está enviando el servidor (ej: "yyyy-MM-dd HH:mm:ss" o "yyyy-MM-dd HH:mm")
        .setDateFormat("yyyy-MM-dd HH:mm")
        .create()

    val api: OrdersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 🚀 CRÍTICO: Añadir el cliente OkHttpClient configurado con el logger
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(OrdersApi::class.java)
    }

    val api_products: ProductsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 🚀 CRÍTICO: Añadir el cliente OkHttpClient configurado con el logger
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(ProductsApi::class.java)
    }

    val api_users: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 🚀 CRÍTICO: Añadir el cliente OkHttpClient configurado con el logger
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(UsersApi::class.java)
    }

    val api_routes: RoutesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // 🚀 CRÍTICO: Añadir el cliente OkHttpClient configurado con el logger
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .build()
            .create(RoutesApi::class.java)
    }
}
