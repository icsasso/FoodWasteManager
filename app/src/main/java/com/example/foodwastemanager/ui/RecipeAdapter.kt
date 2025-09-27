package com.example.foodwastemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.Meal

class RecipeAdapter(private val recipes: List<Meal>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipeName)
        val recipeCategory: TextView = itemView.findViewById(R.id.recipeCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeName.text = recipe.strMeal
        holder.recipeCategory.text = recipe.strCategory ?: "Unknown"
    }

    override fun getItemCount(): Int = recipes.size
}