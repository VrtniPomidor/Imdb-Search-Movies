package com.arsfutura.imdb.domain.repositories

import com.arsfutura.imdb.domain.models.MovieModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface FavoritesRepository {
    fun subscribeToFavorites(): Observable<List<MovieModel>>
    fun errorObserver(): Observable<Throwable>
    fun loadingObserver(): Observable<Boolean>

    fun getFavorites()
    fun toggleFavorite(id: String, refresh: Boolean = true): Single<Boolean>
}