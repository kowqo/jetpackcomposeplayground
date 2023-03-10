package com.example.genshinapp.restaurants.presentation.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.genshinapp.restaurants.domain.GetInitialRestaurantsUseCase
import com.example.genshinapp.restaurants.domain.ToggleRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val getInitialRestaurantsUseCase:
    GetInitialRestaurantsUseCase,
    private val toggleRestaurantUseCase:
    ToggleRestaurantUseCase
) : ViewModel() {
    private var _state by mutableStateOf(
        RestaurantsScreenState(restaurants = listOf(), isLoading = true)
    )

    val state: RestaurantsScreenState
        get() = _state

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state = _state.copy(
            error = exception.message,
            isLoading = false
        )
    }

    init {
        getRestaurants()
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleRestaurantUseCase(id, oldValue)
            _state = _state.copy(restaurants = updatedRestaurants)
        }
    }

    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getInitialRestaurantsUseCase()
            _state = _state.copy(
                restaurants = restaurants,
                isLoading = false
            )
        }
    }
}
