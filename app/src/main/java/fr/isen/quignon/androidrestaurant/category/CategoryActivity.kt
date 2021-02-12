package fr.isen.quignon.androidrestaurant.category

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.quignon.androidrestaurant.HomeActivity
import fr.isen.quignon.androidrestaurant.R
import fr.isen.quignon.androidrestaurant.databinding.ActivityCategoryBinding
import fr.isen.quignon.androidrestaurant.network.NetworkConstant
import fr.isen.quignon.androidrestaurant.network.MenuResult
import fr.isen.quignon.androidrestaurant.network.Dish
import org.json.JSONObject
import com.google.gson.GsonBuilder
import fr.isen.quignon.androidrestaurant.BaseActivity
import fr.isen.quignon.androidrestaurant.detail.DetailActivity
import fr.isen.quignon.androidrestaurant.basket.Basket.Companion.USER_PREFERENCES_NAME
import fr.isen.quignon.androidrestaurant.detail.DishCellClickListener
import fr.isen.quignon.androidrestaurant.utils.Loader

enum class  ItemType{
    STARTER,
    DISH,
    DESSERT;
}

class CategoryActivity : BaseActivity(), DishCellClickListener {

    private lateinit var binding: ActivityCategoryBinding
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = binding.categoryTitleTextView
        val selectedCategory: ItemType? = intent.getSerializableExtra(HomeActivity.CATEGORY_NAME) as? ItemType

        binding.swipeLayout.setOnRefreshListener {
            resetCache()
            loadList(selectedCategory)
        }
        title.text = getCategoryTitle(selectedCategory)
        loadList(selectedCategory)
        Log.d("lifecycle", "onCreate")
    }

    private fun loadList(category: ItemType?) {
        resultFromCache()?.let {
            onSuccess(parseResult(it, category))
        } ?: run {
            val loader = Loader()
            loader.show(this, "Chargement du menu")
            val queue = Volley.newRequestQueue(this)
            val url = NetworkConstant.BASE_URL
            val jsonData = JSONObject()
            jsonData.put("id_shop", "1")
            val request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonData,
                Response.Listener { response ->
                    binding.swipeLayout.isRefreshing = false
                    cacheResult(response.toString())
                    loader.hide(this)
                    onSuccess(parseResult(response.toString(), category))
                },
                Response.ErrorListener { error ->
                    loader.hide(this)
                    binding.swipeLayout.isRefreshing = false
                    onFailure(error)
                }
            )
            queue.add(request)
        }
    }

    private fun cacheResult(response: String) {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(REQUEST_CACHE, response)
        editor.apply()
    }

    private fun resetCache() {
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(REQUEST_CACHE)
        editor.apply()
    }

    private fun resultFromCache(): String? {
        return null
        val sharedPreferences = getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(REQUEST_CACHE, null)
    }

    private fun parseResult(response: String, selectedItem: ItemType?): List<Dish>? {
        val menuResult = GsonBuilder().create().fromJson(response, MenuResult::class.java)
        val items = menuResult.data.firstOrNull { it.name == getCategoryTitleFr(selectedItem) }
        return items?.items
    }

    private fun onSuccess(dishes: List<Dish>?) {
        dishes?.let {
            // CellClickListener = this because this implements CellClickListener
            val adapter = CategoryAdapter(it, this)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }
    }
    private fun onFailure(error: VolleyError) {
        Log.d("Request", error.toString())
    }

    override fun onCellClickListener(data: Dish) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(PLAT, data)
        startActivity(intent)
    }

    private fun getCategoryTitle(item: ItemType?): String {
        return when(item) {
            ItemType.STARTER -> getString(R.string.entree)
            ItemType.DISH -> getString(R.string.plat)
            ItemType.DESSERT -> getString(R.string.dessert)
            else -> ""
        }
    }
    private fun getCategoryTitleFr(item: ItemType?): String {
        return when(item) {
            ItemType.STARTER -> getString(R.string.entree)
            ItemType.DISH -> getString(R.string.plat)
            ItemType.DESSERT -> getString(R.string.dessert)
            else -> ""
        }
    }

    companion object {
        const val PLAT = "DISH"
        const val REQUEST_CACHE = "REQUEST_CACHE"
    }
}