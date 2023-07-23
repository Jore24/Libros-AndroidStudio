package com.cursoklotin.libros.network

import com.cursoklotin.libros.models.Product
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("products/{categoryId}")
    fun getProducts(@Path("categoryId") categoryId: Int): Call<List<Product>>

    @GET("product/{productId}")
    fun getProduct(@Path("productId") productId: Int): Call<Product>

    @POST("products")
    fun insertProduct(@Body product: Product): Call<Product>
}


object RetrofitInstance {
    private const val BASE_URL = "http://192.168.1.26:3000" // Cambiar por tu URL base

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}


