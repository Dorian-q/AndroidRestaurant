package fr.isen.quignon.androidrestaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.quignon.androidrestaurant.category.CategoryAdapter
import fr.isen.quignon.androidrestaurant.databinding.ActivityCategoryBinding

enum class ItemType {
    STARTER, MAIN, DESSERT
}

class CategoryActivity : AppCompatActivity() {

    private lateinit var bindind: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(bindind.root)

        val selectedItem = intent.getSerializableExtra(HomeActivity.CATEGORY_NAME) as? ItemType
        bindind.categoryTitle.text = getCategoryTitle(selectedItem)

        loadList()

        Log.d("lifecycle", "onCreate")
    }

    private fun loadList() {
        var entries = listOf<String>("salade", "boeuf", "glace")
        val adapter = CategoryAdapter(entries)
        bindind.recyclerView.layoutManager = LinearLayoutManager(this)
        bindind.recyclerView.adapter = adapter
    }

    private fun getCategoryTitle(item: ItemType?): String {
        return when(item) {
            ItemType.STARTER -> getString(R.string.starter)
            ItemType.MAIN -> getString(R.string.main)
            ItemType.DESSERT -> getString(R.string.dessert)
            else -> ""
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifecycle", "onResume")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("lifecycle", "onRestart")
    }

    override fun onDestroy() {
        Log.d("lifecycle", "onDestroy")
        super.onDestroy()
    }
}