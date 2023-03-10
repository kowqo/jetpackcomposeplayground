package com.example.genshinapp.restaurants.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.* // ktlint-disable no-wildcard-imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.genshinapp.restaurants.domain.Restaurant
import com.example.genshinapp.restaurants.presentation.Description

@Composable
fun RestaurantsScreen(
    state: RestaurantsScreenState,
    onItemClick: (id: Int) -> Unit = {},
    onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(
                    restaurant = restaurant,
                    onFavoriteClick = { id, oldValue ->
                        onFavoriteClick(id, oldValue)
                    },
                    onItemClick = { id -> onItemClick(id) }
                )
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.semantics {
                this.contentDescription = Description.RESTAURANTS_LOADING
            })
        }
        if (state.error != null) {
            Text(state.error)
        }
    }
}

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit,
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
                onClick = { onFavoriteClick(restaurant.id, restaurant.isFavorite) }
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
    RestaurantsScreen(
        RestaurantsScreenState(listOf(), true),
        {},
        { _, _ -> }
    )
}
