package com.example.foodwastemanager.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodwastemanager.R
import com.example.foodwastemanager.network.BackendClient
import com.example.foodwastemanager.network.InsertResponse
import com.example.foodwastemanager.network.ApiFoodItem  // Import the API model
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Call

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
                saveFoodToDatabase(name, expiration, nameInput, dateInput)
                adapter.addItem(foodItem)

                // Clear inputs
                nameInput.text.clear()
                dateInput.text.clear()
            } else {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFoodToDatabase(name: String, expiration: String, nameInput: EditText, dateInput: EditText) {
        val apiFoodItem = ApiFoodItem(name, expiration)

        // Disable button to prevent multiple clicks
        findViewById<Button>(R.id.saveFoodButton).isEnabled = false

        BackendClient.instance.insertFood(apiFoodItem).enqueue(object : Callback<InsertResponse> {
            override fun onResponse(call: Call<InsertResponse>, response: Response<InsertResponse>) {
                // Re-enable button
                findViewById<Button>(R.id.saveFoodButton).isEnabled = true

                if (response.isSuccessful) {
                    val insertResponse = response.body()
                    Toast.makeText(this@EditFoodActivity, "Food saved successfully!", Toast.LENGTH_SHORT).show()

                    // Add to local adapter for immediate UI update
                    adapter.addItem(com.example.foodwastemanager.ui.FoodItem(name, expiration))

                    // Clear inputs
                    nameInput.text.clear()
                    dateInput.text.clear()
                } else {
                    Toast.makeText(this@EditFoodActivity, "Failed to save food: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<InsertResponse>, t: Throwable) {
                // Re-enable button
                findViewById<Button>(R.id.saveFoodButton).isEnabled = true
                Toast.makeText(this@EditFoodActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
