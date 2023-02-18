package com.example.genshinapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.genshinapp.restaurants.DummyContent
import com.example.genshinapp.restaurants.presentation.Description
import com.example.genshinapp.restaurants.presentation.list.RestaurantsScreen
import com.example.genshinapp.restaurants.presentation.list.RestaurantsScreenState
import com.example.genshinapp.ui.theme.GenshinappTheme
import org.junit.Rule
import org.junit.Test

class RestaurantsScreenTest {
    @get:Rule
    val testRule: ComposeContentTestRule =
        createComposeRule()

    @Test
    fun initialState_isRendered() {
        testRule.setContent {
            GenshinappTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        emptyList(),
                        true
                    ),
                    onFavoriteClick = { _: Int, _: Boolean -> Unit },
                    onItemClick = { _: Int -> Unit }

                )
            }
        }
        testRule.onNodeWithContentDescription(
            Description.RESTAURANTS_LOADING
        ).assertIsDisplayed()
    }

    @Test
    fun stateWithContent_isRendered() {
        val restaurants = DummyContent.getDomainRestaurants()
        testRule.setContent {
            GenshinappTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        restaurants = restaurants,
                        isLoading = false
                    ),
                    onFavoriteClick = { _: Int, _: Boolean -> Unit },
                    onItemClick = { _: Int -> Unit }
                )
            }
        }
        testRule.onNodeWithText(restaurants[0].title).assertIsDisplayed()
        testRule.onNodeWithText(restaurants[0].description).assertIsDisplayed()
        testRule.onNodeWithContentDescription(Description.RESTAURANTS_LOADING).assertDoesNotExist()
    }

    @Test
    fun stateWithContent_ClickOnItem_isRegistred() {
        val restaurants = DummyContent.getDomainRestaurants()
        val targetRestaurant = restaurants[0]
        testRule.setContent {
            GenshinappTheme {
                RestaurantsScreen(
                    state = RestaurantsScreenState(
                        restaurants = restaurants,
                        isLoading = false
                    ),
                    onFavoriteClick = { _: Int, _: Boolean -> Unit },
                    onItemClick = { id: Int -> assert(id == targetRestaurant.id) }
                )
            }
        }

        testRule.onNodeWithText(targetRestaurant.title).performClick()
    }
}
