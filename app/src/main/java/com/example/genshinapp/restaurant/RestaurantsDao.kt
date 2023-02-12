package com.example.genshinapp.restaurant

import androidx.room.*

@Dao
interface RestaurantsDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurans: List<Restaurant>)

    @Update(entity = Restaurant::class)
    suspend fun update(partialRestaurant: PartialRestaurant)

    @Update(entity = Restaurant::class)
    suspend fun updateAll(restaurants: List<PartialRestaurant>)

    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorited(): List<Restaurant>

    @Query("SELECT * FROM restaurants where r_id = :id")
    suspend fun getRestaurant(id: Int): Restaurant
}
