package com.arsfutura.imdb.domain.repositories

import com.arsfutura.imdb.domain.models.MovieModel
import io.reactivex.rxjava3.core.Observable

interface MovieListRepository {
    fun subscribeToMovieList(): Observable<List<MovieModel>>
    fun errorObserver(): Observable<Throwable>
    fun loadingObserver(): Observable<Boolean>

    fun nextPage(search: String)
    fun getMovies(search: String)
    fun refreshLocalList()
}