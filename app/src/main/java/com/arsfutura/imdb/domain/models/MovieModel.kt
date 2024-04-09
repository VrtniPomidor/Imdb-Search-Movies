package com.arsfutura.imdb.domain.models

open class MovieModel(
    val movieId: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val year: String = "",
    var isFavorite: Boolean = false
)

data class MovieHeaderModel(
    val headerTitle: String,
) : MovieModel()

data class MovieDetailsModel(
    val movieId: String = "",
    val imageUrl: String = "",
    val title: String = "",
    val description: String = "",
    val year: String = "",
    var isFavorite: Boolean = false,
    val released: String = "",
    val runtime: String = "",
    val genre: String = "",
    val rating: String = "",
)
