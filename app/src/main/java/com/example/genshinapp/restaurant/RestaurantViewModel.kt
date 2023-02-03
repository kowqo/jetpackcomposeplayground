package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.api.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantViewModel (savedStateHandle: SavedStateHandle): ViewModel() {
    private var restInterface: RestaurantsApiService
    var state by mutableStateOf<Restaurant?>(null)


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurantcompose-96296-default-rtdb.europe-west1.firebasedatabase.app/")
            .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)
        val id=savedStateHandle.get<Int>("restraunt_id")?:0

        viewModelScope.launch{
            val restaurant = getRemoteRestaurant(id)
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