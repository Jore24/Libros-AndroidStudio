package com.cursoklotin.libros.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.cursoklotin.libros.R

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val btnViewProducts: Button = findViewById(R.id.btnViewProducts)
        val btnInsertProductsDetails: Button = findViewById(R.id.btnInsertProductsDetails)
        val btnInsertProducts: Button = findViewById(R.id.btnInsertProducts)
        val btnSoldProductsList: Button = findViewById(R.id.btnSoldProductsList)
        val btnViewArticles: Button = findViewById(R.id.btnViewArticles)

        btnViewArticles.setOnClickListener {
            Intent(this, ViewArticles::class.java).also {
                startActivity(it)
            }
        }

        btnViewProducts.setOnClickListener {
            Intent(this, ViewProducts::class.java).also {
                startActivity(it)
            }
        }

        btnInsertProductsDetails.setOnClickListener {
            Intent(this, InsertProductsWithDetails::class.java).also {
                startActivity(it)
            }

        }

        btnInsertProducts.setOnClickListener {
            //insertar productos tan solo con la foto y descripción
            Intent(this, InsertProductsWithoutDetails ::class.java).also {
                startActivity(it)
            }

        }

        btnSoldProductsList.setOnClickListener {
            Intent(this, SoldProducts::class.java).also {
                startActivity(it)
            }
        }
    }
}