package com.example.medisupplyapp.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("product_id") val productId: Int,
    @SerializedName("sku") val sku: String,
    @SerializedName("total_quantity") val totalQuantity: Int,
    @SerializedName("value") val value: Double,
    @SerializedName("name") val name: String
)

data class ProductRequest(
    val product_id: Int,
    val name: String,
    val sku: String,
    val category_name: String,
    val total_quantity: Int,
    val value: Double,
    val image_url: String?,
    val quantity: Int,
    val price_unit: Double
)

fun ProductSuggestion.toProduct(): Product {
    return Product(
        categoryName = this.category_name,
        productId = this.product_id,
        sku = this.sku,
        totalQuantity = this.total_quantity,
        value = this.value,
        name = this.name
    )
}

fun List<ProductSuggestion>.toProductList(): List<Product> {
    return this.map { it.toProduct() }
}

data class ProductUpdate(
    val product_id: Int,
    val quantity: Int
)

data class ProductUpdateRequest(
    val products: List<ProductUpdate>
)

data class UpdateResponse(
    val message: String,
    val updated: Int
)
