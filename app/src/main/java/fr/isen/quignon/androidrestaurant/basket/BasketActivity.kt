package fr.isen.quignon.androidrestaurant.basket

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.quignon.androidrestaurant.HomeActivity
import fr.isen.quignon.androidrestaurant.R
import fr.isen.quignon.androidrestaurant.databinding.ActivityBasketBinding
import fr.isen.quignon.androidrestaurant.detail.DetailViewFragment
import fr.isen.quignon.androidrestaurant.network.NetworkConstant
import fr.isen.quignon.androidrestaurant.network.RegisterResult
import fr.isen.quignon.androidrestaurant.registration.RegisterFragment
import fr.isen.quignon.androidrestaurant.registration.UserActivity
import com.google.gson.GsonBuilder
import org.json.JSONObject

class BasketActivity : AppCompatActivity(), BasketCellInterface {
    lateinit var binding: ActivityBasketBinding
    private lateinit var basket: Basket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        basket = Basket.getBasket(this)

        val fragment = BasketItemsFragment(basket, this)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()

        binding.orderButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivityForResult(intent, UserActivity.REQUEST_CODE)
        }
    }

    override fun onDeleteItem(item: BasketItem) {
        basket.items.remove(item)
        basket.save(this)
    }

    override fun onShowDetail(item: BasketItem) {
        val fragment = DetailViewFragment(item.dish)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == UserActivity.REQUEST_CODE && resultCode == Activity.RESULT_FIRST_USER) {
            val sharedPreferences = getSharedPreferences(UserActivity.USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val idUser = sharedPreferences.getInt(UserActivity.ID_USER, -1)
            if(idUser != -1) {
                sendOrder(idUser)
            }
        }
    }

    private fun sendOrder(idUser: Int) {
        val message = basket.items.map { "${it.count}x ${it.dish.name}" }.joinToString("\n")

        val queue = Volley.newRequestQueue(this)
        val url = NetworkConstant.BASE_URL + NetworkConstant.PATH_ORDER

        val jsonData = JSONObject()
        jsonData.put(NetworkConstant.ID_SHOP, "1")
        jsonData.put(NetworkConstant.ID_USER, idUser)
        jsonData.put(NetworkConstant.MSG, message)

        var request = JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonData,
                { response ->
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Votre commande a bien été prise en compte")
                    builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
                        basket.clear()
                        basket.save(this)
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    builder.show()
                },
                { error ->
                    error.message?.let {
                        Log.d("request", it)
                    } ?: run {
                        Log.d("request", error.toString())
                        Log.d("request", String(error.networkResponse.data))
                    }
                }
        )
        queue.add(request)
    }
}