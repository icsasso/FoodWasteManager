package com.example.foodwastemanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.Meal
import com.example.foodwastemanager.model.MealResponse
import com.example.foodwastemanager.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var recipeImage: ImageView
    private lateinit var recipeName: TextView
    private lateinit var recipeCategory: TextView
    private lateinit var recipeInstructions: TextView
    private lateinit var recipeIngredients: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_view)

        recipeImage = findViewById(R.id.recipeImage)
        recipeName = findViewById(R.id.recipeName)
        recipeCategory = findViewById(R.id.recipeCategory)
        recipeInstructions = findViewById(R.id.recipeInstructions)
        recipeIngredients = findViewById(R.id.recipeIngredients)

        val mealId = intent.getStringExtra("MEAL_ID")
        if (mealId == null) {
            Toast.makeText(this, "No meal ID provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchMealDetails(mealId)

        val returnHomeButton = findViewById<ImageButton>(R.id.returnToSuggestionsButton)
        returnHomeButton.setOnClickListener {
            val intent = Intent(this, RecipeRecommendationsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchMealDetails(mealId: String) {
        val api = RetrofitClient.instance
        api.getMealById(mealId).enqueue(object : Callback<MealResponse> {
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    if (!meals.isNullOrEmpty()) {
                        displayMeal(meals[0])
                    } else {
                        Toast.makeText(this@RecipeDetailActivity, "No meal details found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RecipeDetailActivity, "Failed to load meal details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Toast.makeText(this@RecipeDetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayMeal(meal: Meal) {
        // Set main info
        recipeName.text = meal.strMeal
        recipeCategory.text = "${meal.strCategory ?: ""} | ${meal.strArea ?: ""}"
        recipeInstructions.text = meal.strInstructions ?: "No instructions provided"

        // Load image
        Glide.with(this)
            .load(meal.strMealThumb)
            .placeholder(R.drawable.ic_launcher_background)
            .into(recipeImage)

        // Collect ingredients + measures using public properties
        val ingredients = listOfNotNull(
            meal.strIngredient1, meal.strIngredient2, meal.strIngredient3, meal.strIngredient4,
            meal.strIngredient5, meal.strIngredient6, meal.strIngredient7, meal.strIngredient8,
            meal.strIngredient9, meal.strIngredient10, meal.strIngredient11, meal.strIngredient12,
            meal.strIngredient13
        )

        val measures = listOfNotNull(
            meal.strMeasure1, meal.strMeasure2, meal.strMeasure3, meal.strMeasure4,
            meal.strMeasure5, meal.strMeasure6, meal.strMeasure7, meal.strMeasure8,
            meal.strMeasure9, meal.strMeasure10, meal.strMeasure11, meal.strMeasure12,
            meal.strMeasure13
        )

        val ingredientText = ingredients.mapIndexed { index, ing ->
            val measure = measures.getOrNull(index) ?: ""
            "• $ing - $measure"
        }.joinToString("\n")

        recipeIngredients.text = ingredientText
    }
}
