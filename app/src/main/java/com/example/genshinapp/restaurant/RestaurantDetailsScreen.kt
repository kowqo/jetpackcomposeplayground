package com.example.genshinapp.restaurant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RestaurantDetailsScreen() {
    val viewModel: RestaurantViewModel = viewModel()
    val item = viewModel.state
    if (item != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                RestaurantIcon(
                    icon = Icons.Filled.Place,
                    Modifier.padding(
                        top =32.dp,
                        bottom = 32.dp
                    )
                )
                RestaurantDetails(
                    modifier =  Modifier.padding(bottom = 32.dp),
                    title = item.title,
                    description = item.description,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                Text( text = "More text coming soon")
            }

    }
}
