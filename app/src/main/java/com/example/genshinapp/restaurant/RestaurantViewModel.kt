package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    var state by mutableStateOf<Restaurant?>(null)
    private val restaurantDao = RestaurantsDb.getDaoInstance(
        RestaurantsApplication.getAppContext()
    )

    init {

        val id = savedStateHandle.get<String>("restaurant_id")?.toInt() ?: 0

        viewModelScope.launch {
            val restaurant = getCachedRestaurant(id)
            state = restaurant
        }
    }

    private suspend fun getCachedRestaurant(id: Int): Restaurant =
        withContext(Dispatchers.IO) {
            val restaurant = restaurantDao.getRestaurant(id)
            return@withContext restaurant
        }
}
