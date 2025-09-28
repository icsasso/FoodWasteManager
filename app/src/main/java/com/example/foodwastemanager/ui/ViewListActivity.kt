package com.example.foodwastemanager.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.network.BackendClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeButton: Button
    private lateinit var adapter: SelectableFoodAdapter
    


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_list)

        recyclerView = findViewById(R.id.recipeRecyclerView)
        recipeButton = findViewById(R.id.viewRecipeSuggestionsButton)

        // Initially disable recipe button
        recipeButton.isEnabled = false
        recipeButton.alpha = 0.5f

        // TODO: Replace with your saved food list (maybe from database or shared prefs)
        fetchFoodFromDatabase()

        adapter = SelectableFoodAdapter(emptyList()) { hasSelection ->
            recipeButton.isEnabled = hasSelection
            recipeButton.alpha = if (hasSelection) 1f else 0.5f
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recipeButton.setOnClickListener {
            if (recipeButton.isEnabled) {
                val intent = Intent(this, RecipeRecommendationsActivity::class.java)
                startActivity(intent)
            }
        }
        val returnHomeButton = findViewById<ImageButton>(R.id.returnHomeButton)
        returnHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // This clears the back stack so MainActivity is on top
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    private fun fetchFoodFromDatabase() {
        BackendClient.instance.getItems().enqueue(object : Callback<List<Map<String, Any>>> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<List<Map<String, Any>>>,
                response: Response<List<Map<String, Any>>>
            ) {
                if (response.isSuccessful) {
                    val items = response.body() ?: emptyList()

                    // Convert backend response into FoodItem list
                    val foodItems = items.map {
                        val name = it["name"] as? String ?: "Unknown"
                        val expiration = it["expiration_date"] as? String ?: "Unknown"
                        FoodItem(name, expiration)
                    }

                    adapter.updateList(foodItems) // 🔥 you’ll need this method in SelectableFoodAdapter
                } else {
                    Toast.makeText(this@ViewListActivity, "Failed to fetch items", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Toast.makeText(this@ViewListActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
