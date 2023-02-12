package com.example.genshinapp.restaurant

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RestaurantsScreen(onItemClick: (id: Int) -> Unit = {}) {
    val viewModel: RestaurantsViewModel = viewModel()

    LazyColumn(contentPadding = PaddingValues(8.dp)) {
        items(viewModel.state) { restaurant ->
            RestaurantItem(
                restaurant = restaurant,
                onFavoriteClick = { id -> viewModel.toggleFavorite(id) },
                onItemClick = { id -> onItemClick(id) }
            )
        }
    }
}

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onFavoriteClick: (id: Int) -> Unit,
    onItemClick: (id: Int) -> Unit
) {
    val icon = if (restaurant.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(restaurant.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(
                icon = Icons.Default.Place,
                modifier = Modifier.weight(0.15f)
            )
            RestaurantDetails(
                title = restaurant.title,
                description = restaurant.description,
                modifier = Modifier.weight(0.7f)
            )
            RestaurantIcon(
                modifier = Modifier.weight(0.15f),
                icon = icon,
                onClick = { onFavoriteClick(restaurant.id) }
            )
        }
    }
}

@Composable
fun RestaurantDetails(
    modifier: Modifier = Modifier,
    title: String = "",
    description: String = "",
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        CompositionLocalProvider(LocalContentColor provides Color.Black.copy(alpha = 0.2f)) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RestaurantIcon(
    icon: ImageVector = Icons.Default.Place,
    modifier: Modifier,
    onClick: () -> Unit = { }
) {
    Image(
        imageVector = icon,
        contentDescription = null,
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RestaurantsScreen()
}
