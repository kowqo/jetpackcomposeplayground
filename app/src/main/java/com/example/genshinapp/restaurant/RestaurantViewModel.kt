package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.api.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantViewModel : ViewModel() {
    private var restInterface: RestaurantsApiService
    var state by mutableStateOf<Restaurant?>(null)


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurantcompose-96296-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)

        viewModelScope.launch{
            val restaurant = getRemoteRestaurant(2)
            state= restaurant
        }
    }
    private suspend fun getRemoteRestaurant(id:Int):Restaurant{
        return withContext(Dispatchers.IO){
            val responseMap = restInterface.getRestaurant(id)
            return@withContext responseMap.values.first()
        }
    }
}