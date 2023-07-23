package com.cursoklotin.libros.models

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("CategoryId")
    val CategoryId: Int,
    @SerializedName("CategoryName")
    val CategoryName: String,
    // Otros campos que recibas desde el backend
)