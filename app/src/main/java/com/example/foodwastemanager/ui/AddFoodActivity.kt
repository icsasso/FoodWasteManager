package com.example.foodwastemanager.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwastemanager.R
import com.example.foodwastemanager.api.RetrofitClient
import com.example.foodwastemanager.models.FoodItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        val nameInput = findViewById<EditText>(R.id.foodNameEditText)
        val dateInput = findViewById<EditText>(R.id.expirationDateEditText)
        val addButton = findViewById<Button>(R.id.addFoodButton)

        val api = RetrofitClient.instance

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val date = dateInput.text.toString()

            if (name.isBlank() || date.isBlank()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newItem = FoodItem(name, date)

            api.insertFood(newItem).enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddFoodActivity, "Food added!", Toast.LENGTH_SHORT).show()
                        nameInput.text.clear()
                        dateInput.text.clear()
                    } else {
                        Toast.makeText(this@AddFoodActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(this@AddFoodActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}