package com.example.foodwastemanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R

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
        val items = listOf(
            FoodItem("Milk", "09/28/2025"),
            FoodItem("Eggs", "10/01/2025"),
            FoodItem("Cheese", "09/29/2025")
        )

        adapter = SelectableFoodAdapter(items) { hasSelection ->
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
    }
}
