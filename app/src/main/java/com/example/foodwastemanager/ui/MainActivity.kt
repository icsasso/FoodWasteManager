package com.example.foodwastemanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodwastemanager.R
import com.example.foodwastemanager.model.MealResponse
import com.example.foodwastemanager.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupButton()
    }

    private fun setupButton() {
        // Add/Edit Food List button
        val addFoodButton = findViewById<Button>(R.id.addFoodButton)
        addFoodButton.setOnClickListener {
            val intent = Intent(this, EditFoodActivity::class.java)
            startActivity(intent)
        }

        // View Food List button
        val viewListButton = findViewById<Button>(R.id.viewFoodListButton)
        viewListButton.setOnClickListener {
            val intent = Intent(this, ViewListActivity::class.java)
            startActivity(intent)
        }
    }

}


