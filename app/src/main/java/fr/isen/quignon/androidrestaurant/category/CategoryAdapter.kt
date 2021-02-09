package fr.isen.quignon.androidrestaurant.category

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import fr.isen.quignon.androidrestaurant.databinding.DishesCellBinding
import fr.isen.quignon.androidrestaurant.network.Dish
import fr.isen.quignon.androidrestaurant.R
import com.squareup.picasso.Picasso

class CategoryAdapter(private val entries: List<Dish>,
                      private val entryClickListener: (Dish) -> Unit)
    : RecyclerView.Adapter<CategoryAdapter.DishesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishesViewHolder {
        return DishesViewHolder(DishesCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DishesViewHolder, position: Int) {
        val dish = entries[position]
        holder.layout.setOnClickListener {
            entryClickListener.invoke(dish)
        }
        holder.bind(dish)
    }

    override fun getItemCount(): Int {
        return entries.count()
    }

    class DishesViewHolder(dishesBinding: DishesCellBinding): RecyclerView.ViewHolder(dishesBinding.root) {
        val titleView: TextView = dishesBinding.dishesTitle
        val priceView: TextView = dishesBinding.dishPrice
        val imageView: ImageView = dishesBinding.dishImageView
        val layout = dishesBinding.root

        fun bind(dish: Dish) {
            titleView.text = dish.name
            priceView.text = "${dish.prices.first().price} €"
            Picasso.get()
                .load(dish.getThumbnailUrl())
                .placeholder(R.drawable.logo_resto)
                .into(imageView)
        }
    }
}