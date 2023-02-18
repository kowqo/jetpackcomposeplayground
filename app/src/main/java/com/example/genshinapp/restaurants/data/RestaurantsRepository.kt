package com.example.genshinapp.restaurants.data

import com.example.genshinapp.restaurants.data.local.LocalRestaurant
import com.example.genshinapp.restaurants.data.local.PartialLocalRestaurant
import com.example.genshinapp.restaurants.data.local.RestaurantsDao
import com.example.genshinapp.restaurants.data.remote.RestaurantsApiService
import com.example.genshinapp.restaurants.domain.Restaurant
import java.lang.Exception
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class RestaurantsRepository @Inject constructor(
    private val restInterface: RestaurantsApiService,
    private val restaurantsDao: RestaurantsDao
) {

    suspend fun getRestaurants(): List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll().map {
                Restaurant(it.id, it.title, it.description, it.isFavorite)
            }
        }
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoritedRestaurants = restaurantsDao.getAllFavorited()
        restaurantsDao.addAll(
            remoteRestaurants.map {
                LocalRestaurant(it.id, it.title, it.description, false)
            }
        )
        restaurantsDao.updateAll(
            favoritedRestaurants.map {
                PartialLocalRestaurant(it.id, true)
            }
        )
    }

    suspend fun loadRestaurants() {
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
        }
    }

    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(PartialLocalRestaurant(id = id, isFavorite = value))
        }
}
