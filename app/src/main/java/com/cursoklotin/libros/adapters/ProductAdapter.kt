import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.cursoklotin.libros.R
import com.cursoklotin.libros.activitys.ViewArticles
import com.cursoklotin.libros.activitys.ViewProducts
import com.cursoklotin.libros.models.Product
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var originalProductList: List<Product> = mutableListOf()

//    fun setData(newData: List<Product>) {
//        productList = newData
//        notifyDataSetChanged()
//    }

    fun filterProducts(searchTerm: String) {
        if (searchTerm.isEmpty()) {
            // Si el término de búsqueda está vacío, mostramos todos los productos sin filtrar.
            productList = originalProductList
        } else {
            // Si hay un término de búsqueda, filtramos la lista original.
            productList = originalProductList.filter { product ->
                product.title.contains(searchTerm, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

    fun setOriginalProductList(products: List<Product>) {
        originalProductList = products
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.textViewNombre.text = product.title
        holder.textViewPrecio.text = product.price.toString()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        try {
            val date = inputFormat.parse(product.created_at)
            holder.textViewFechaDeCreacion.text = "Fecha de creación: ${outputFormat.format(date)}"
            println("Valor de product.created_at: ${product.created_at}") // Imprimir por consola
        } catch (e: ParseException) {
            holder.textViewFechaDeCreacion.text = "Fecha de creación: Error en formato"
            println("Excepción al convertir fecha: ${e.message}") // Imprimir por consola la excepción
            e.printStackTrace() // Imprimir la traza completa del error
        }

        // Para ver detalles de productos o artículos
        holder.btnVerDetalles.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Ver detalles: ${product.productId}", Toast.LENGTH_SHORT).show()

            // Verificar si estamos en ViewProducts o ViewArticles y llamar a la función correspondiente
            val context = holder.itemView.context
            if (context is ViewProducts) {
                context.onVerDetallesProducto1(product.productId)
            } else if (context is ViewArticles) {
                context.onVerDetallesArticle(product.productId)
            }
        }

        // Falta configurar
        holder.btnEditar.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Editar: ${product.productId}", Toast.LENGTH_SHORT).show()
            println("Valor de product.productId: ${product.productId}")
        }

        // Falta configurar
        holder.btnEliminar.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Eliminar: ${product.productId}", Toast.LENGTH_SHORT).show()
            println("Valor de product.productId: ${product.productId}")
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombre)
        val textViewPrecio: TextView = itemView.findViewById(R.id.textViewPrecio)
        val textViewFechaDeCreacion: TextView = itemView.findViewById(R.id.textViewFechaDeCreacion)
        val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }
}
