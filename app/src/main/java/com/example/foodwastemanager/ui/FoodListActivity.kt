package com.example.foodwastemanager.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwastemanager.R
import com.example.foodwastemanager.api.RetrofitClient
import com.example.foodwastemanager.models.FoodItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodListActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private val api = RetrofitClient.instance
    private val items = mutableListOf<String>()  // just storing names for now
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        listView = findViewById(R.id.foodListView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        loadFoodList()

        listView.setOnItemClickListener { _, _, position, _ ->
            val itemName = items[position]
            api.deleteFood(itemName).enqueue(object : Callback<Map<String, String>> {
                override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@FoodListActivity, "$itemName deleted", Toast.LENGTH_SHORT).show()
                        items.removeAt(position)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@FoodListActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                    Toast.makeText(this@FoodListActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadFoodList() {
        api.getFoodList().enqueue(object : Callback<Map<String, List<FoodItem>>> {
            override fun onResponse(
                call: Call<Map<String, List<FoodItem>>>,
                response: Response<Map<String, List<FoodItem>>>
            ) {
                if (response.isSuccessful) {
                    items.clear()
                    response.body()?.get("food_list")?.forEach { items.add(it.name) }
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@FoodListActivity, "Error loading list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, List<FoodItem>>>, t: Throwable) {
                Toast.makeText(this@FoodListActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}