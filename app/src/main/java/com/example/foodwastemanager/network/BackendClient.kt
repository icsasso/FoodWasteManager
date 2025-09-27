package com.example.foodwastemanager.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
// In your network package
object BackendClient {
    private const val BASE_URL = "https://monica-decayable-archiepiscopally.ngrok-free.dev/"  // For emulator

    val instance: BackendApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApiService::class.java)
    }
}