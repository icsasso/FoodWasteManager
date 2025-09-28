package com.example.foodwastemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.Meal

class RecipeRecommendationsAdapter(private val recipes: List<Meal>) :
    RecyclerView.Adapter<RecipeRecommendationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val recipeIngredients: TextView = itemView.findViewById(R.id.recipeIngredients)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.strMeal ?: "Unknown"
        holder.recipeIngredients.text = recipe.strCategory ?: "Unknown Category"
    }

    override fun getItemCount(): Int = recipes.size
}