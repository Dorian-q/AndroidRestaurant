package fr.isen.quignon.androidrestaurant.category

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import fr.isen.quignon.androidrestaurant.databinding.DishCellBinding
import fr.isen.quignon.androidrestaurant.detail.DishCellClickListener
import fr.isen.quignon.androidrestaurant.network.Dish

class CategoryAdapter(private val entries: List<Dish>,
                      private val cellClickListener: DishCellClickListener):
    RecyclerView.Adapter<CategoryAdapter.DishViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        return DishViewHolder(DishCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        holder.dishTitle.text = entries[position].name.take(40)
        holder.dishPrice.text = "${entries[position].prices[0].price}€"
        if (entries[position].images[0].isNotEmpty()) {
            Picasso.get().load(entries[position].images[0]).into(holder.dishImageView);
        }
        holder.layout.setOnClickListener {
            cellClickListener.onCellClickListener(entries[position])
        }
    }
    override fun getItemCount(): Int {
        return entries.count()
    }

    class DishViewHolder (dishBinding: DishCellBinding): RecyclerView.ViewHolder(dishBinding.root) {

        val dishTitle: TextView= dishBinding.dishTitle
        val dishPrice: TextView= dishBinding.dishPrice
        val dishImageView: ImageView= dishBinding.dishImageView
        val layout: CardView = dishBinding.root
    }
}