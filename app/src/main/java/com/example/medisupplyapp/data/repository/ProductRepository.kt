package com.example.medisupplyapp.data.repository

import com.example.medisupplyapp.data.model.Product

class ProductRepository {
    suspend fun fetchProducts(): List<Product> {
        val response = RetrofitInstance.api.getProducts()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener productos: ${response.code()}")
        }
    }
}

