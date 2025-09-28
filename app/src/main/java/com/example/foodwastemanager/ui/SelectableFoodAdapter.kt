package com.example.foodwastemanager.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class SelectableFoodAdapter(
    private var foodList: List<FoodItem>,
    private val onItemSelected: (Boolean) -> Unit
) : RecyclerView.Adapter<SelectableFoodAdapter.FoodViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.foodCheckBox)
        val expiration: TextView = itemView.findViewById(R.id.foodExpiration)
        val warningIcon: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_item_selectable, parent, false)
        return FoodViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val item = foodList[position]

        holder.checkBox.text = item.name
        holder.expiration.text = "Expires: ${item.expiration}"

        // Parse expiration date to decide warning visibility
        try {
            val expDate = LocalDate.parse(item.expiration, formatter)
            val daysUntilExpiry = ChronoUnit.DAYS.between(LocalDate.now(), expDate)
            holder.warningIcon.visibility = if (daysUntilExpiry <= 2) View.VISIBLE else View.GONE
        } catch (e: Exception) {
            holder.warningIcon.visibility = View.GONE
        }

        // Handle "only one selection"
        holder.checkBox.isChecked = position == selectedPosition
        holder.checkBox.setOnClickListener {
            val oldPos = selectedPosition
            selectedPosition = if (holder.checkBox.isChecked) position else RecyclerView.NO_POSITION
            notifyItemChanged(oldPos)
            notifyItemChanged(selectedPosition)
            onItemSelected(selectedPosition != RecyclerView.NO_POSITION)
        }
    }

    override fun getItemCount(): Int = foodList.size

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateList(newList: List<FoodItem>) {
        foodList = newList.sortedBy {
            try {
                LocalDate.parse(it.expiration, formatter)
            } catch (e: Exception) {
                LocalDate.MAX
            }
        }
        notifyDataSetChanged()
    }



}
