package com.arsfutura.imdb.presentation.movielist

import com.arsfutura.imdb.data.repo.MovieListRepositoryImpl
import com.arsfutura.imdb.domain.repositories.MovieListRepository
import org.koin.dsl.module

val movieListModule = module {
    single<MovieListRepository> { MovieListRepositoryImpl(get(), get()) }

    factory { (view: MovieListMvp.View) ->
        MovieListPresenter(get(), get(), view)
    }
}