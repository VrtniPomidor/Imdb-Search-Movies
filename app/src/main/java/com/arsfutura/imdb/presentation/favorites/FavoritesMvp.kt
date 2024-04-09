package com.arsfutura.imdb.presentation.favorites

import com.arsfutura.imdb.domain.models.MovieModel
import com.arsfutura.imdb.presentation.common.BaseMvp


interface FavoritesMvp {
    interface View : BaseMvp.View {
        fun loadFavorites(favorites: List<MovieModel>)
        fun setLoading(isLoading: Boolean)
    }

    interface Presenter : BaseMvp.Presenter {
        fun toggleFavoriteMovie(movie: MovieModel)
        fun refresh()
    }
}