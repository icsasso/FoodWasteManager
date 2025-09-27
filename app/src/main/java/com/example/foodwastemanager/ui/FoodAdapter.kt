package com.example.foodwastemanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R

class FoodAdapter(
    private val items: MutableList<FoodItem>
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.foodTitle)
        val expiration: TextView = itemView.findViewById(R.id.foodExpiration)
        val deleteButton: ImageButton = itemView.findViewById(R.id.imageButton2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = items[position]
        holder.title.text = food.name
        holder.expiration.text = food.expiration

        // Delete functionality
        holder.deleteButton.setOnClickListener {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount() = items.size

    // Add item function
    fun addItem(food: FoodItem) {
        items.add(food)
        notifyItemInserted(items.size - 1)
    }
}
