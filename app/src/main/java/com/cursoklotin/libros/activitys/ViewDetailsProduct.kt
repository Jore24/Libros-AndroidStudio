package com.cursoklotin.libros.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.cursoklotin.libros.R
import com.cursoklotin.libros.models.Product
import com.cursoklotin.libros.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.bumptech.glide.Glide
import com.cursoklotin.libros.adapters.ProductImageAdapter


class ViewDetailsProduct : AppCompatActivity() {

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_details_product)

        // Recibir datos del producto seleccionado en el recyclerview de la activity main
        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        println("Valor de productId en ViewDetailsProduct: $productId")
        if (productId != -1) {
            // Aquí puedes usar el productId para cargar los detalles del producto desde la base de datos o realizar otras acciones relacionadas con ese producto
            getProductDetails(productId)
        } else {
            // No se recibió el productId correctamente, muestra un mensaje de error o realiza alguna acción en consecuencia
            Toast.makeText(this, "Error al recibir el productId", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getProductDetails(productId: Int) {
        // Realizar la solicitud HTTP para obtener los detalles del producto
        val call = RetrofitInstance.apiService.getProduct(productId)

        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    val product = response.body()
                    if (product != null) {
                        // Mostrar los detalles del producto en el Logcat
                        Log.d("ViewDetailsProduct", "Detalles del producto:")
                        Log.d("ViewDetailsProduct", "Product ID: ${product.productId}")
                        Log.d("ViewDetailsProduct", "Title: ${product.title}")
                        Log.d("ViewDetailsProduct", "Author: ${product.author}")
                        Log.d("ViewDetailsProduct", "Year: ${product.year}")
                        Log.d("ViewDetailsProduct", "Publisher: ${product.publisher}")
                        Log.d("ViewDetailsProduct", "Description: ${product.description}")
                        Log.d("ViewDetailsProduct", "Status: ${product.status}")
                        Log.d("ViewDetailsProduct", "Price: ${product.price}")
                        //Log.d("ViewDetailsProduct", "Image URL: ${product.image}")

                        // Configurar los TextViews con los detalles del producto
                        findViewById<TextView>(R.id.textViewProductId).text = "Product ID: ${product.productId}"
                        findViewById<TextView>(R.id.textViewTitle).text = "Title: ${product.title}"
                        findViewById<TextView>(R.id.textViewAuthor).text = "Author: ${product.author}"
                        findViewById<TextView>(R.id.textViewYear).text = "Year: ${product.year}"
                        findViewById<TextView>(R.id.textViewPublisher).text = "Publisher: ${product.publisher}"
                        findViewById<TextView>(R.id.textViewDescription).text = "Description: ${product.description}"
                        findViewById<TextView>(R.id.textViewStatus).text = "Status: ${product.status}"
                        findViewById<TextView>(R.id.textViewPrice).text = "Price: ${product.price}"

                        val viewPager: ViewPager = findViewById(R.id.viewPagerProductImages)
                        val imageUrls = product.image.split("  -  ") // Suponiendo que las URL están separadas por "  -  "
                        val imageAdapter = ProductImageAdapter(imageUrls)
                        viewPager.adapter = imageAdapter
                    } else {
                        Log.e("ViewDetailsProduct", "Producto no encontrado")
                    }
                } else {
                    Log.e("ViewDetailsProduct", "Error al obtener detalles del producto")
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("ViewDetailsProduct", "Error de conexión")
            }
        })
    }
}
