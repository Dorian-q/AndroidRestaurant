package fr.isen.quignon.androidrestaurant.network

import com.google.gson.annotations.SerializedName

class Ingredient(@SerializedName("name_fr") val name: String) {}