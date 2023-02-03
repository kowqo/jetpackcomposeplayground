package com.example.genshinapp.restaurant

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Restaurant::class],
    version = 1,
    exportSchema = false
)
abstract class RestaurantsDb : RoomDatabase()