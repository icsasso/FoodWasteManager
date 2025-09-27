package com.example.foodwastemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.MealResponse
import com.example.foodwastemanager.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        fetchMeals()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.mealsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchMeals() {
        RetrofitClient.instance.searchMeals("")
            .enqueue(object : Callback<MealResponse> {
                override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        mealAdapter = MealAdapter(meals)
                        findViewById<RecyclerView>(R.id.mealsRecyclerView).adapter = mealAdapter
                        meals.forEach {
                            Log.d("MealAPI", "Meal: ${it.strMeal}")
                        }
                    } else {
                        Log.d("MealAPI", "No meals found")
                    }
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Log.e("MealAPI", "Error: ${t.localizedMessage ?: "Unknown error"}")
                }
            })
    }
}