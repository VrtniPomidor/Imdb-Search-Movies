package com.arsfutura.imdb.data.models

import com.google.gson.annotations.SerializedName


class MoviesWrapperResponse {
    @SerializedName("Search")
    val searches: List<MovieDataResponse> = arrayListOf()

    @SerializedName("totalResults")
    val totalResults: String = ""

    @SerializedName("Error")
    val error: String = ""
}

class MovieDataResponse {
    @SerializedName("imdbID")
    val imdbId: String = ""

    @SerializedName("Title")
    val title: String = ""

    @SerializedName("Year")
    val year: String = ""

    @SerializedName("Type")
    val type: String = ""

    @SerializedName("Poster")
    val poster: String = ""
}