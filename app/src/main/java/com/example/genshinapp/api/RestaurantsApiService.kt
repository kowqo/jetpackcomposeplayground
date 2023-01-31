package com.example.genshinapp.api

import com.example.genshinapp.restaurant.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantsApiService {
    @GET("restaurants.json")
    suspend fun getRestaurants():List<Restaurant>
}