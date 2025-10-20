package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.remote.api.ProductsApi

class ProductRepository(var api: ProductsApi) {
    suspend fun fetchProducts(): List<Product> {
        val response = api.getProducts()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener productos: ${response.code()}")
        }
    }
}