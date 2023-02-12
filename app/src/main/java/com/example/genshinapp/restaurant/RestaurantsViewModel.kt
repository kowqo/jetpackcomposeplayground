package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.api.RestaurantsApiService
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel() : ViewModel() {

    private val restInterface: RestaurantsApiService
    private val restaurantsDao = RestaurantsDb.getDaoInstance(
        RestaurantsApplication.getAppContext()
    )

    var state by mutableStateOf(emptyList<Restaurant>())

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            // добавить конвертор
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            // урл базы
            .baseUrl(
                "https://restaurantcompose-96296-default-rtdb.europe-west1.firebasedatabase.app/"
            )
            .build()
        // затолкать в экземляр реализацию ретрофита
        restInterface = retrofit.create(
            RestaurantsApiService::class.java
        )
        getRestaurants()
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(id, oldValue)
            state = updatedRestaurants
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            state = getAllRestaurants()
        }
    }

    private suspend fun getAllRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (
                e: Exception
            ) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty()) {
                            throw Exception("Something went wrong" + "we have no data to display")
                        }
                    }
                    else -> throw e
                }
            }
            return@withContext restaurantsDao.getAll()
        }
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoritedRestaurants = restaurantsDao.getAllFavorited()
        restaurantsDao.addAll(remoteRestaurants)
        restaurantsDao.updateAll(
            favoritedRestaurants.map {
                PartialRestaurant(it.id, true)
            }
        )
    }

    private suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(PartialRestaurant(id = id, isFavorite = !oldValue))
            restaurantsDao.getAll()
        }
}
