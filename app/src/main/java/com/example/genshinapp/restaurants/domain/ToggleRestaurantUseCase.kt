package com.example.genshinapp.restaurants.domain

import com.example.genshinapp.restaurants.data.RestaurantsRepository

class ToggleRestaurantUseCase {
    private val repository: RestaurantsRepository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase =
        GetSortedRestaurantsUseCase()

    suspend operator fun invoke(
        id: Int,
        value: Boolean
    ): List<Restaurant> {
        val newFav = value.not()
        repository.toggleFavoriteRestaurant(id, newFav)
        return getSortedRestaurantsUseCase()
    }
}
