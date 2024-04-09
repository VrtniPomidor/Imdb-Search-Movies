package com.arsfutura.imdb.data.local

import androidx.room.*
import com.arsfutura.imdb.data.entity.Favorite

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun getAll(): List<Favorite>

    @Query("SELECT * FROM favorite WHERE id = :imdbId")
    fun isFavorite(imdbId: String): Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun favorite(vararg favorite: Favorite)

    @Delete
    fun unFavorite(vararg favorite: Favorite)
}