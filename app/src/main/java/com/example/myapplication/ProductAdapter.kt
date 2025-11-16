import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.ProfileFragment
import com.example.myapplication.R

class ProductAdapter(private val productList: List<Product>, private val onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private val useAltLayout: Boolean = false // 👈 добавляем
    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val name: TextView = itemView.findViewById(R.id.productName)
        val price: TextView = itemView.findViewById(R.id.productPrice)
        val image: ImageView = itemView.findViewById(R.id.productImage)
        val popular = itemView.findViewById<TextView>(R.id.productPopular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layout = if (parent.id == R.id.search_recycler || parent.id == R.id.FPrecyclerView)
            R.layout.item_producttwo
        else
            R.layout.item_products


        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = productList[position]
        if (product.name == "") {
            holder.image.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"))
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }else{ holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#FDEF78"))
            holder.image.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"))}
        holder.name.text = product.name.take(14)

        holder.price.text = product.price
        if (product.popular?.toInt() != 1488){holder.popular.text = "In cart: " + (product.popular?.toInt()?.times((-1))).toString()}else{holder.popular.text = ""}

        when (val photoSource = product.imageUrl) {
            is String -> {
                // Если это строка (URL), загружаем из сети
                Glide.with(holder.itemView.context)
                    .load(photoSource)
                    .into(holder.image)
            }
            is Int -> {
                // Если это число (ID ресурса), загружаем из drawable
                Glide.with(holder.itemView.context)
                    .load(photoSource)
                    .into(holder.image)
            }
            else -> {
                // (Опционально) Устанавливаем изображение по умолчанию, если тип неизвестен
                holder.image.setImageResource(R.drawable.icon) // Замените на ваш плейсхолдер
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }



    override fun getItemCount(): Int = productList.size
}
