package com.cursoklotin.libros.models


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("ProductId")
    val productId: Int,
    @SerializedName("CategoryId")
    val categoryId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("year")
    val year: String,
    @SerializedName("publisher")
    val publisher: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("image")
    var image: String, // Cambiar de String a List<String>
    @SerializedName("price")
    val price: Double,
    @SerializedName("created_at")
    val created_at: String,
)
