package com.arsfutura.imdb.di

import com.arsfutura.imdb.data.di.networkModule
import com.arsfutura.imdb.db.databaseModule
import com.arsfutura.imdb.presentation.moviedetails.movieDetailsModule
import com.arsfutura.imdb.presentation.movielist.movieListModule
import com.arsfutura.imdb.presentation.favorites.searchModule

val applicationModules = arrayListOf(
    databaseModule,
    networkModule,
    searchModule,
    movieListModule,
    movieDetailsModule
)