package com.example.foodwastemanager.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendRetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"


    val instance: FoodApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FoodApi::class.java)
    }
}
