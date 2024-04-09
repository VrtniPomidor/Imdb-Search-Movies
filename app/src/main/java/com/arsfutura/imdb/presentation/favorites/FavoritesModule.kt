package com.arsfutura.imdb.presentation.favorites

import com.arsfutura.imdb.data.repo.FavoritesRepositoryImpl
import com.arsfutura.imdb.domain.repositories.FavoritesRepository
import org.koin.dsl.module

val searchModule = module {
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get(), get()) }

    factory { (view: FavoritesMvp.View) ->
        FavoritesPresenter(get(), view)
    }
}