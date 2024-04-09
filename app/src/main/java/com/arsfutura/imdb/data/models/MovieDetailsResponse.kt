package com.arsfutura.imdb.data.models

import com.google.gson.annotations.SerializedName

class MovieDetailsResponse {
    @SerializedName("imdbID")
    val imdbId: String = ""

    @SerializedName("Title")
    val title: String = ""

    @SerializedName("Year")
    val year: String = ""

    @SerializedName("Released")
    val released: String = ""

    @SerializedName("Runtime")
    val runtime: String = ""

    @SerializedName("Genre")
    val genre: String = ""

    @SerializedName("Plot")
    val plot: String = ""

    @SerializedName("Poster")
    val poster: String = ""

    @SerializedName("Ratings")
    val ratings: List<RatingsResponse> = arrayListOf()
}

class RatingsResponse {
    @SerializedName("Source")
    val source: String = ""

    @SerializedName("Value")
    val value: String = ""
}