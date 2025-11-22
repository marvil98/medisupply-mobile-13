package com.example.medisupplyapp.data.remote.repository

import com.example.medisupplyapp.data.model.Product
import com.example.medisupplyapp.data.model.ProductUpdate
import com.example.medisupplyapp.data.model.ProductUpdateRequest
import com.example.medisupplyapp.data.model.UpdateResponse
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

    suspend fun updateProductStock(productId: Int, quantity: Int): UpdateResponse {
        val request = ProductUpdateRequest(
            products = listOf(ProductUpdate(productId, quantity))
        )
        val response = api.updateStock(request)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        } else {
            throw Exception("Error al actualizar stock: ${response.code()}")
        }
    }
}