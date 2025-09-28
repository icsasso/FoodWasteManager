// BackendApiService.kt
package com.example.foodwastemanager.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.foodwastemanager.network.ApiFoodItem

// Data classes for API communication
data class ApiFoodItem(
    val name: String,
    val expiration_date: String
)
data class InsertResponse(
    val message: String,
    val item: ApiFoodItem,
    val id: String
)

data class DeleteResponse(
    val message: String
)

data class ErrorResponse(
    val detail: String
)

interface BackendApiService {
    @GET("/")
    fun ping(): Call<Map<String, String>>

    @GET("/items")
    fun getItems(): Call<List<Map<String, Any>>>

    @POST("/insert")
    fun insertFood(@Body foodItem: ApiFoodItem): Call<InsertResponse>

    @DELETE("/delete/{item_name}")
    fun deleteFood(@Path("item_name") itemName: String): Call<DeleteResponse>
}