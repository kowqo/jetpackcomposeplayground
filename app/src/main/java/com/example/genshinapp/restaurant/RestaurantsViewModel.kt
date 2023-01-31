package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.api.RestaurantsApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    private val restInterface: RestaurantsApiService
    var state by mutableStateOf(emptyList<Restaurant>())

    private val errorHandler = CoroutineExceptionHandler { _, exception ->

        exception.printStackTrace()
    }


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            //добавить конвертор
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            //урл базы
            .baseUrl(
                "https://restaurantcompose-96296-default-rtdb.europe-west1.firebasedatabase.app/"
            )
            .build()
        //затолкать в экземляр реализацию ретрофита
        restInterface = retrofit.create(
            RestaurantsApiService::class.java
        )

        getRestaurants()
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getRemoteRestaurants()
            //show rest
            state = restaurants.restoreSelections()
        }
    }

    suspend fun getRemoteRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            restInterface.getRestaurants()
        }

    }


    fun toggleFavorite(id: Int): Unit {
        val restaurants = state.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] = item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state = restaurants
    }

    private fun List<Restaurant>.restoreSelections(): List<Restaurant> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }

            return restaurantsMap.values.toList()
        }
        return this
    }

    private fun storeSelection(item: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }


    companion object {
        const val FAVORITES = "favorites"
    }
}