package com.example.foodwastemanager.network

import com.example.foodwastemanager.model.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface MealApi {
    @GET ("search.php")
    fun searchMeals(@Query("s") name: String): Call<MealResponse>

    @GET("random.php")
    fun getRandomMeal(): Call <MealResponse>

    @GET(value="filter.php")
    fun filterMealsByIngredient(@Query("i") ingredient: String): Call<MealResponse>
}