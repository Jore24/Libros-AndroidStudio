package com.cursoklotin.libros.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("idUsers")
    val idUsers: Int,
    @SerializedName("name")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("role")
    val role: String,
    //val created_at: String,
    //val updated_at: String
    // Otros campos que recibas desde el backend
)