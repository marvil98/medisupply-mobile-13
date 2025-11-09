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
    @SerializedName("product_id") val productId: String,
    @SerializedName("quantity") val quantity: Int
)

fun ProductSuggestion.toProduct(): Product {
    return Product(
        categoryName = this.category_name,
        productId = this.product_id.toString(),
        sku = this.sku,
        totalQuantity = this.total_quantity,
        value = this.value,
        name = this.name
    )
}

fun List<ProductSuggestion>.toProductList(): List<Product> {
    return this.map { it.toProduct() }
}