package fr.isen.quignon.androidrestaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import fr.isen.quignon.androidrestaurant.category.CategoryActivity
import fr.isen.quignon.androidrestaurant.category.ItemType
import fr.isen.quignon.androidrestaurant.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.starter.setOnClickListener {
            statCategoryActivity(ItemType.STARTER)
        }

        binding.main.setOnClickListener {
            statCategoryActivity(ItemType.MAIN)
        }

        binding.dessert.setOnClickListener {
            statCategoryActivity(ItemType.DESSERT)
        }

    }

    private fun statCategoryActivity(item: ItemType) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CATEGORY_NAME, item)
        startActivity(intent)
    }

    companion object {
        const val CATEGORY_NAME = "CATEGORY_NAME"
    }
}
