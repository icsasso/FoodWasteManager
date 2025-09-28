package com.example.foodwastemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.Meal
import android.content.Intent

class RecipeRecommendationsAdapter(private val recipes: List<Meal>) :
    RecyclerView.Adapter<RecipeRecommendationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImage)
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val recipeIngredients: TextView = itemView.findViewById(R.id.recipeIngredients)
        val recipeCategory: TextView = itemView.findViewById(R.id.recipeCategory)
        val viewRecipeButton: Button = itemView.findViewById(R.id.viewRecipeButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.recipeName.text = recipe.strMeal ?: "Unknown Recipe"
        holder.recipeIngredients.text = recipe.strCategory ?: ""

        // Load recipe image using Glide
        Glide.with(holder.itemView.context)
            .load(recipe.strMealThumb)
            .placeholder(R.drawable.ic_launcher_background) // placeholder image
            .into(holder.recipeImage)

        // Button click event
        holder.viewRecipeButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecipeDetailActivity::class.java)
            intent.putExtra("MEAL_ID", recipe.idMeal)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int = recipes.size
}