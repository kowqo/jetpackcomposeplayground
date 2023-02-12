package com.example.genshinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.genshinapp.restaurant.RestaurantDetailsScreen
import com.example.genshinapp.restaurant.RestaurantsScreen
import com.example.genshinapp.ui.theme.GenshinappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GenshinappTheme {
                RestaurantApp()
            }
        }
    }
}

@Composable
private fun RestaurantApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "restaurants") {
        composable(route = "restaurants") {
            RestaurantsScreen {
                    id ->
                navController.navigate("restaurants/$id")
            }
        }
        composable(
            route = "restaurants/{restaurant_id}",
            deepLinks = listOf(
                navDeepLink { uriPattern = "www.restaurantsapp.details.com/{restaurant_id}" }
            ),
            arguments = listOf(
                navArgument("restaurant_id") {
                    type = NavType.StringType
                }
            )
        ) { navStackEntry ->

            RestaurantDetailsScreen()
        }
    }
}
