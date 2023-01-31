package com.example.genshinapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.genshinapp.restaurant.Restaurant
import com.example.genshinapp.restaurant.RestaurantDetailsScreen
import com.example.genshinapp.restaurant.RestaurantsScreen
import com.example.genshinapp.ui.theme.GenshinappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GenshinappTheme {
                    RestaurantsScreen()
//                RestaurantDetailsScreen()
            }
        }
    }
}
@Composable
private fun RestaurantApp(){

}