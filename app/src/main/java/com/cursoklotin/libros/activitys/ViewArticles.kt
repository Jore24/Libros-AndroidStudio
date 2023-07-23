package com.cursoklotin.libros.activitys

import ProductAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursoklotin.libros.R
import com.cursoklotin.libros.models.Product
import com.cursoklotin.libros.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewArticles : AppCompatActivity() {

    private val apiService = RetrofitInstance.apiService
    private lateinit var btnAddProduct: ImageButton
    private val ADD_PRODUCT_REQUEST_CODE = 100
    private val productList: MutableList<Product> = mutableListOf()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var btnAtras: ImageButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_products)

        btnAtras = findViewById(R.id.btnAtras)
        btnAtras.setOnClickListener {
            Intent(this, Home::class.java).also {
                startActivity(it)
            }
        }

        editTextSearch = findViewById(R.id.editTextSearch)

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No es necesario realizar ninguna acción antes de cambiar el texto.
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                // Aquí realizaremos la búsqueda de artículos según el texto ingresado.
                val searchTerm = charSequence.toString().trim()
                productAdapter.filterProducts(searchTerm)
            }

            override fun afterTextChanged(editable: Editable?) {
                // No es necesario realizar ninguna acción después de cambiar el texto.
            }
        })

        btnAddProduct = findViewById(R.id.btnInsertar)

        btnAddProduct.setOnClickListener {
            val intent = Intent(this, InsertProductsWithDetails::class.java)
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Configura el Adapter con la lista de artículos vacía
        productAdapter = ProductAdapter(productList)
        productAdapter.setOriginalProductList(productList)

        recyclerView.adapter = productAdapter

        // Obtiene la lista de artículos mediante Retrofit
        fetchArticleList(2) // El categoryId para artículos es 2
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PRODUCT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Obtener la lista de artículos actualizada solo si se agregó un nuevo artículo
            fetchArticleList(2) // El categoryId para artículos es 2
        }
    }

    fun onVerDetallesArticle(articleId: Int) {
        val intent = Intent(this, ViewDetailsArticles::class.java)
        intent.putExtra(ViewDetailsArticles.EXTRA_ARTICLE_ID, articleId)
        startActivity(intent)
    }

    private fun fetchArticleList(categoryId : Int) {
        val call = apiService.getProducts(categoryId)

        call.enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val articles = response.body()
                    if (articles != null) {
                        // Actualiza la lista de artículos y notifica al Adapter
                        productList.clear()
                        productList.addAll(articles)
                        productAdapter.notifyDataSetChanged()
                        println("Valor de articles: $articles")
                    }
                } else {
                    // Manejar errores de respuesta del servidor
                    showToast("Error: ${response.code()}")
                    val errorsito = response.errorBody()?.string()
                    println("Valor de errorsito: $errorsito")
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                // Manejar errores de conexión o solicitud fallida
                showToast("Error: ${t.message}")
                val errorsito = t.message.toString()
                println("Valor de errorsito2: $errorsito")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
