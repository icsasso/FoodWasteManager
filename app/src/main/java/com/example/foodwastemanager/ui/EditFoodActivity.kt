package com.example.foodwastemanager.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R

class EditFoodActivity : AppCompatActivity() {

    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_food)

        val recyclerView = findViewById<RecyclerView>(R.id.foodRecyclerView)
        val nameInput = findViewById<EditText>(R.id.foodNameInput)
        val dateInput = findViewById<EditText>(R.id.expirationDateInput)
        val saveButton = findViewById<Button>(R.id.saveFoodButton)

        // Setup RecyclerView
        adapter = FoodAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Add Food
        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val expiration = dateInput.text.toString().trim()

            if (name.isNotEmpty() && expiration.isNotEmpty()) {
                val foodItem = FoodItem(name, expiration)
                adapter.addItem(foodItem)

                // Clear inputs
                nameInput.text.clear()
                dateInput.text.clear()
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
