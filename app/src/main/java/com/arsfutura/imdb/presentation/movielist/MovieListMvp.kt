package com.arsfutura.imdb.presentation.movielist

import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.presentation.common.BaseMvp


interface MovieListMvp {
    interface View : BaseMvp.View {
        fun loadMovies(movies: List<MovieModel>)
        fun setLoading(isLoading: Boolean)
    }

    interface Presenter : BaseMvp.Presenter {
        fun searchMovies(text: String)
        fun toggleFavoriteMovie(movie: MovieModel)
        fun nextPage(search: String)
    }
}