package com.example.medisupplyapp.data.remote

import com.example.medisupplyapp.data.remote.api.ProductsApi
import com.example.medisupplyapp.data.remote.api.UsersApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api_users: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApi::class.java)
    }

    val api_products: ProductsApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.3:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductsApi::class.java)
    }
}