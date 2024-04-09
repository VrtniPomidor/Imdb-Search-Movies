package com.arsfutura.imdb.presentation.moviedetails

import com.arsfutura.imdb.data.repo.MovieDetailsRepositoryImpl
import com.arsfutura.imdb.domain.repositories.MovieDetailsRepository
import org.koin.dsl.module

val movieDetailsModule = module {
    scope<MovieDetailsFragment> {
        scoped<MovieDetailsRepository> { MovieDetailsRepositoryImpl(get(), get()) }
        scoped { (view: MovieDetailsMvp.View) ->
            MovieDetailsPresenter(get(), get(), view)
        }
    }
}