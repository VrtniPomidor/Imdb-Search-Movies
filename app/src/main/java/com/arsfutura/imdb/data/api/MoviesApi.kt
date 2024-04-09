package com.arsfutura.imdb.data.api

import com.arsfutura.imdb.data.models.MovieDetailsResponse
import com.arsfutura.imdb.data.models.MoviesWrapperResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    @GET("/")
    fun getMoviesByTitle(
        @Query("s") title: String,
        @Query("page") page: Int,
    ): Single<MoviesWrapperResponse>

    @GET("/")
    fun getMovie(
        @Query("i") id: String
    ): Single<MovieDetailsResponse>
}