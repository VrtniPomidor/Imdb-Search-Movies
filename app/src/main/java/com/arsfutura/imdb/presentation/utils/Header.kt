package com.arsfutura.imdb.presentation.utils

import com.arsfutura.imdb.domain.models.MovieHeaderModel
import com.arsfutura.imdb.domain.models.MovieModel

fun createAdapterData(list: List<MovieModel>): List<MovieModel> {
    var year = ""
    val temp: ArrayList<MovieModel> = arrayListOf()
    for (movie in list) {
        if (movie.year != year) {
            year = movie.year
            temp.add(MovieHeaderModel(year))
        }
        temp.add(movie)
    }
    return temp
}