package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("product_id") val productId: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("total_quantity") val totalQuantity: Int,
    @SerializedName("value") val value: Double
)
