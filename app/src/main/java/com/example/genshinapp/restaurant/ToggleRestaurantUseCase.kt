package com.example.genshinapp.restaurant

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
