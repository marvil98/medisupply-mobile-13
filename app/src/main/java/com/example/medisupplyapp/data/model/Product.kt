package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("total_quantity") val totalQuantity: Int,
    @SerializedName("value") val value: Double,
    @SerializedName("name") val name: String
)

data class ProductRequest(
    @SerializedName("product_id") val productId: Int,
    @SerializedName("quantity") val quantity: Int
)