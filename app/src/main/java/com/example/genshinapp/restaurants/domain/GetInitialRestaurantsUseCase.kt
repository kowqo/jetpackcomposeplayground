package com.example.genshinapp.restaurants.domain

import com.example.genshinapp.restaurants.data.RestaurantsRepository

class GetInitialRestaurantsUseCase {
    private val repository: RestaurantsRepository = RestaurantsRepository()
    private val getSortedRestaurantsUseCase: GetSortedRestaurantsUseCase = GetSortedRestaurantsUseCase()
    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}
