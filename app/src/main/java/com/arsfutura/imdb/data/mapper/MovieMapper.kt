package com.arsfutura.imdb.data.mapper

import com.arsfutura.imdb.data.local.FavoriteDao
import com.arsfutura.imdb.data.models.MovieDetailsResponse
import com.arsfutura.imdb.data.models.MoviesWrapperResponse
import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.domain.models.MovieModel

class MovieMapper {
    fun toMovieModel(
        movieDetails: MoviesWrapperResponse,
        favoriteDao: FavoriteDao
    ): List<MovieModel> {
        val movieModels: ArrayList<MovieModel> = arrayListOf()
        for (movie in movieDetails.searches) {
            movieModels.add(
                MovieModel(
                    movieId = movie.imdbId,
                    title = movie.title,
                    year = movie.year,
                    isFavorite = favoriteDao.isFavorite(movie.imdbId) != null,
                    imageUrl = movie.poster,
                )
            )
        }
        return movieModels
    }

    fun toMovieModel(
        movie: MovieDetailsResponse,
    ): MovieModel {
        return MovieModel(
            movieId = movie.imdbId,
            title = movie.title,
            year = movie.year,
            isFavorite = true,
            imageUrl = movie.poster,
        )
    }

    fun toMovieDetailsModel(movie: MovieDetailsResponse, isFavorite: Boolean): MovieDetailsModel {
        return MovieDetailsModel(
            movieId = movie.imdbId,
            title = movie.title,
            description = movie.plot,
            year = movie.year,
            imageUrl = movie.poster,
            released = movie.released,
            runtime = movie.runtime,
            genre = movie.genre,
            rating = if (movie.ratings.isNotEmpty()) {
                movie.ratings[0].value
            } else "N/A",
            isFavorite = isFavorite,
        )
    }
}