package com.arsfutura.imdb.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arsfutura.imdb.data.entity.Favorite
import com.arsfutura.imdb.data.local.FavoriteDao

@Database(entities = [Favorite::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}