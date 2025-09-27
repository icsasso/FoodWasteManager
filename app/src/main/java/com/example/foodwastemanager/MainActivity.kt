package com.example.foodwastemanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.foodwastemanager.ui.AddFoodActivity
import com.example.foodwastemanager.ui.FoodListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addFoodButton = findViewById<Button>(R.id.addFoodButton)
        val viewListButton = findViewById<Button>(R.id.viewListButton)

        addFoodButton.setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        viewListButton.setOnClickListener {
            startActivity(Intent(this, FoodListActivity::class.java))
        }
    }
}