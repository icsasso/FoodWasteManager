package com.example.foodwastemanager.network

import retrofit2.Call
import retrofit2.http.GET
// In your network package
interface BackendApiService {
    @GET("/")
    fun ping(): Call<Map<String, String>>
}