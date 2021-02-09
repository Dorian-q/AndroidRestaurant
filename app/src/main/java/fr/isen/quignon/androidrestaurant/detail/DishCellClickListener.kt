package fr.isen.quignon.androidrestaurant.detail

import fr.isen.quignon.androidrestaurant.network.Dish

interface DishCellClickListener {
    fun onCellClickListener(data: Dish)
}