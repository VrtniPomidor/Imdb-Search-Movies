package com.arsfutura.imdb.presentation.moviedetails

import com.arsfutura.imdb.domain.models.MovieDetailsModel
import com.arsfutura.imdb.presentation.common.BaseMvp


interface MovieDetailsMvp {
    interface View : BaseMvp.View {
        fun loadMovie(movie: MovieDetailsModel)
        fun setLoading(isLoading: Boolean)
        fun setFavorite(isFavorite: Boolean)
    }

    interface Presenter : BaseMvp.Presenter {
        fun getDetailsFor(id: String)
        fun toggleFavoriteMovie()
    }
}