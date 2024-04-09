package com.arsfutura.imdb.domain.repositories

import com.arsfutura.imdb.domain.models.MovieDetailsModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface MovieDetailsRepository {
    fun subscribeToMovieDetails(): Observable<MovieDetailsModel>
    fun errorObserver(): Observable<Throwable>
    fun loadingObserver(): Observable<Boolean>

    fun getMovieWithId(id: String)
}