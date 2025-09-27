package com.example.foodwastemanager.network

import com.example.foodwastemanager.models.FoodItem
import retrofit2.Call
import retrofit2.http.*

interface FoodApi {


    @POST("insert")
    fun insertFood(@Body item: FoodItem): Call<Map<String, Any>>


    @GET("list")
    fun getFoodList(): Call<Map<String, List<FoodItem>>>


    @DELETE("delete/{name}")
    fun deleteFood(@Path("name") name: String): Call<Map<String, String>>
}