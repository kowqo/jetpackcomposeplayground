package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.api.RestaurantsApiService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    private val restInterface: RestaurantsApiService
    private val restaurantsDao = RestaurantsDb.getDaoInstance(
        RestaurantsApplication.getAppContext(),
    )
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
            val restaurants = getAllRestaurants()
            //show rest
            state = restaurants.restoreSelections()
        }
    }

    private suspend fun getAllRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                val restaurants = restInterface.getRestaurants()
                restaurantsDao.addAll(restaurants)
                return@withContext restaurants
            } catch (
                e: Exception
            ) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        return@withContext restaurantsDao.getAll()
                    }

                    else -> throw e
                }
            }
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