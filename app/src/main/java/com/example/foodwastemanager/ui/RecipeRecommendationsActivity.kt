package com.example.foodwastemanager.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.MealResponse
import com.example.foodwastemanager.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeRecommendationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Link to recipe_recommendations.xml
        setContentView(R.layout.recipe_recommendations)

        val recyclerView = findViewById<RecyclerView>(R.id.recipeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load recipes into RecyclerView
        fetchRecipes(recyclerView)
    }

    private fun fetchRecipes(recyclerView: RecyclerView) {
        RetrofitClient.instance.searchMeals("")
            .enqueue(object : Callback<MealResponse> {
                override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                    val recipes = response.body()?.meals ?: emptyList()
                    if (recipes.isNotEmpty()) {
                        recyclerView.adapter = RecipeRecommendationsAdapter(recipes)
                        Log.d("RecipeRecommendations", "Loaded ${recipes.size} recipes")
                    } else {
                        Log.d("RecipeRecommendations", "No recipes found")
                    }
                }

                override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                    Log.e("RecipeRecommendations", "Error fetching recipes: ${t.localizedMessage}")
                }
            })
    }
}