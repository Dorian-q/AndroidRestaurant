package fr.isen.quignon.androidrestaurant.basket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.quignon.androidrestaurant.databinding.BasketCellBinding
import com.squareup.picasso.Picasso

class BasketAdapter(private val items: List<BasketItem>, private val basketCellClickListener: BasketCellClickListener): RecyclerView.Adapter<BasketAdapter.BasketItemsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketItemsViewHolder {
        return BasketItemsViewHolder(BasketCellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BasketItemsViewHolder, position: Int) {
        holder.dishName.text = items[position].dish.name.take(40)
        holder.dishPrice.text = "${items[position].dish.prices[0].price.toFloat() * items[position].itemCount} €"
        if (items[position].dish.images[0].isNotEmpty()) {
            Picasso.get().load(items[position].dish.images[0]).into(holder.dishImage)
        }
        holder.btnDelete.setOnClickListener {
            basketCellClickListener.onDeleteItem(items[position])
        }
        holder.dishQuantity.text = "${items[position].itemCount} * ${items[position].dish.prices[0].price.toFloat()}"
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    class BasketItemsViewHolder(basketCellBinding: BasketCellBinding): RecyclerView.ViewHolder(basketCellBinding.root) {
        val dishName: TextView = basketCellBinding.basketItemTitle
        val dishPrice: TextView = basketCellBinding.basketItemPrice
        val dishImage: ImageView = basketCellBinding.basketItemImageView
        val dishQuantity: TextView = basketCellBinding.basketItemQuantity
        val btnDelete: ImageView = basketCellBinding.basketItemDelete
    }
}