package com.example.foodwastemanager.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object
RetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    val instance: MealApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MealApi::class.java)
    }
}