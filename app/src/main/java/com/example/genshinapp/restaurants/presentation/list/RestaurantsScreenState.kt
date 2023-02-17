package com.example.genshinapp.restaurants.presentation.list

import com.example.genshinapp.restaurants.domain.Restaurant

data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null
)
