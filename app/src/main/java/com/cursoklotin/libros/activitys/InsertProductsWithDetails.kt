package com.cursoklotin.libros.activitys

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursoklotin.libros.R
import com.cursoklotin.libros.adapters.ImageAdapter
import com.cursoklotin.libros.models.Product
import com.cursoklotin.libros.network.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertProductsWithDetails : AppCompatActivity() {
    private lateinit var btn_Atras: ImageButton
    private lateinit var imgProducts: ImageView
    private lateinit var txtImages: ImageButton
    private lateinit var txtTile: TextInputEditText
    private lateinit var txtAuthor: TextInputEditText
    private lateinit var txtYear: TextInputEditText
    private lateinit var txtPublisher: TextInputEditText
    private lateinit var txtDescription: TextInputEditText
    private lateinit var txtStatus: TextInputEditText
    private lateinit var txtPrice: TextInputEditText
    private lateinit var btnGuardar: Button
    private lateinit var radioGroupCategory: RadioGroup
    private lateinit var radioButtonArticle: RadioButton
    private lateinit var radioButtonBook: RadioButton
    private lateinit var recyclerViewImages: RecyclerView
    private val selectedImages = ArrayList<Uri>()
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_products)

        btn_Atras = findViewById(R.id.btn_Atras)
        imgProducts = findViewById(R.id.imgProducts)
        txtImages = findViewById(R.id.txtImages)
        txtTile = findViewById(R.id.txtTile)
        txtAuthor = findViewById(R.id.txtAuthor)
        txtYear = findViewById(R.id.txtYear)
        txtPublisher = findViewById(R.id.txtPublisher)
        txtDescription = findViewById(R.id.txtDescription)
        txtStatus = findViewById(R.id.txtStatus)
        txtPrice = findViewById(R.id.txtPrice)
        btnGuardar = findViewById(R.id.btnGuardar)
        radioGroupCategory = findViewById(R.id.radioGroupCategory)
        radioButtonArticle = findViewById(R.id.radioButtonArticle)
        radioButtonBook = findViewById(R.id.radioButtonBook)

        storageReference = FirebaseStorage.getInstance().reference

        recyclerViewImages = findViewById(R.id.recyclerViewImages)
        recyclerViewImages.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter(selectedImages)
        recyclerViewImages.adapter = imageAdapter


        radioGroupCategory.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonArticle -> {
                    // El usuario seleccionó la categoría "Artículo"
                    // Puedes realizar acciones específicas para esta categoría aquí
                }
                R.id.radioButtonBook -> {
                    // El usuario seleccionó la categoría "Libro"
                    // Puedes realizar acciones específicas para esta categoría aquí
                }
            }
        }


        // Configurar el click listener para seleccionar imágenes
        txtImages.setOnClickListener {
            selectImages()
            Toast.makeText(this, "Seleccionar imágenes", Toast.LENGTH_SHORT).show()
        }

        // Configurar el click listener para guardar el producto
        btnGuardar.setOnClickListener {
            // Obtener los datos del producto desde las vistas
            val title = txtTile.text.toString().trim()
            val author = txtAuthor.text.toString().trim()
            val year = txtYear.text.toString().trim()
            val publisher = txtPublisher.text.toString().trim()
            val description = txtDescription.text.toString().trim()
            val status = txtStatus.text.toString().trim()
            val priceText = txtPrice.text.toString().trim()

            val selectedCategoryId = radioGroupCategory.checkedRadioButtonId


            if (title.isNotEmpty() && author.isNotEmpty() && year.isNotEmpty() && publisher.isNotEmpty() &&
                description.isNotEmpty() && status.isNotEmpty() && priceText.isNotEmpty()
            ) {
                val price = priceText.toDouble()

                val categoryId = if (selectedCategoryId == R.id.radioButtonBook) {
                    1 // ID de categoría para "Libro"
                } else {
                    2 // ID de categoría para "Artículo"
                }

                val product = Product(
                    productId = 0, // Este valor se asignará en el backend
                    categoryId = categoryId, // Reemplaza con el ID de categoría correspondiente
                    title = title,
                    author = author,
                    year = year,
                    publisher = publisher,
                    description = description,
                    status = status,
                    image = "", // La URL de la imagen se asignará después de subirla a Firebase Storage
                    price = price,
                    created_at = "" // Este valor se asignará en el backend
                )

                // Insertar el producto en el backend
                insertProduct(product)
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para seleccionar imágenes desde la galería
    private fun selectImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        // Pasar las imágenes seleccionadas previamente como resultado
        if (selectedImages.isNotEmpty()) {
            intent.putParcelableArrayListExtra(Intent.EXTRA_INITIAL_INTENTS, selectedImages)
        }

        resultLauncher.launch(Intent.createChooser(intent, "Seleccionar imágenes"))
    }

    // Activity Result Launcher para recibir las imágenes seleccionadas desde la galería
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            if (data != null) {
                if (data.clipData != null) {
                    val clipData = data.clipData
                    val newSelectedImages = ArrayList<Uri>()
                    for (i in 0 until clipData!!.itemCount) {
                        val imageUri = clipData.getItemAt(i).uri
                        newSelectedImages.add(imageUri)

                        // Imprimir la ruta de la imagen seleccionada
                        println("Ruta de la imagen $i: $imageUri")
                    }

                    // Reemplazar las imágenes seleccionadas anteriores con las nuevas
                    selectedImages.clear()
                    selectedImages.addAll(newSelectedImages)

                    // Notificar al adaptador del RecyclerView que los datos han cambiado
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    // Método para insertar el producto en el backend
    private fun insertProduct(product: Product) {
        // Obtener el nombre de las imágenes en Firebase Storage (puedes generar nombres únicos usando UUID)
        val imageNames = ArrayList<String>()
        for (i in 0 until selectedImages.size) {
            val imageName = "image_${System.currentTimeMillis()}_$i.jpg"
            imageNames.add(imageName)
        }

        // Crear referencias para las imágenes en Firebase Storage
        val imageUrls = ArrayList<String>()
        for (i in 0 until selectedImages.size) {
            val imageRef = storageReference.child(imageNames[i])
            imageRef.putFile(selectedImages[i])
                .addOnSuccessListener {
                    // Imagen subida exitosamente, obtener la URL de descarga
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        imageUrls.add(uri.toString())

                        // Verificar si todas las imágenes han sido subidas
                        if (imageUrls.size == selectedImages.size) {
                            // Todas las imágenes han sido subidas, ahora puedes enviar los datos al backend
                            // Unir las URLs en una sola cadena separada por comas
                            val imagesString = imageUrls.joinToString("  -  ")

                            // Asignar la cadena de URLs de las imágenes al campo "image" del producto
                            product.image = imagesString

                            // Continuar con el envío del producto al backend
                            insertProductToBackend(product)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Error al subir la imagen
                    Toast.makeText(this, "Error al subir la imagen $i", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Método para enviar el producto al backend
    private fun insertProductToBackend(product: Product) {
        // Aquí debes implementar el código para enviar los datos del producto (incluyendo la URL de la imagen) al backend
        // Puedes usar Retrofit, Volley, o cualquier otra librería para realizar la solicitud HTTP al servidor

        val gson = Gson()
        val productJson = gson.toJson(product)
        println("JSON enviado al servidor: $productJson")
        val apiService = RetrofitInstance.apiService

        apiService.insertProduct(product).enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@InsertProductsWithDetails, "Producto agregado exitosamente", Toast.LENGTH_SHORT).show()
                    val resultIntent = Intent()
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(this@InsertProductsWithDetails, "Error al agregar el producto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Toast.makeText(this@InsertProductsWithDetails, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
