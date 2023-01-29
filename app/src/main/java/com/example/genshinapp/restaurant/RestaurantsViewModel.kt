package com.example.genshinapp.restaurant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {

    var state by mutableStateOf(dummyRestaurants.restoreSelections())

    fun toggleFavorite(id: Int):Unit {
        val restaurants = state.toMutableList()
        val itemIndex = restaurants.indexOfFirst { it-> it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] = item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state=restaurants
    }

    private fun List<Restaurant>.restoreSelections() :List<Restaurant>{
        stateHandle.get<List<Int>?>(FAVORITES)?.let{
                selectedIds->
            val restaurantsMap =this.associateBy { it.id }
            selectedIds.forEach{id->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }

    private fun storeSelection (item : Restaurant){
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }




    companion object{
        const val FAVORITES = "favorites"
    }
}