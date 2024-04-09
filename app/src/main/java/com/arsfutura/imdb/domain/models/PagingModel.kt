package com.arsfutura.imdb.domain.models

class PagingModel(
    search: String,
    val page: Int
) {
    val search: String = search.trim()
}
