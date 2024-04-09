package com.arsfutura.imdb.presentation.utils

import android.util.Log
import com.arsfutura.imdb.domain.models.MovieModel

class SortMoviesByYear : Comparator<MovieModel> {
    override fun compare(o1: MovieModel?, o2: MovieModel?): Int {
        val year1: Int = o1?.year?.parseInt() ?: 0
        val year2: Int = o2?.year?.parseInt() ?: 0
        if (year1 > year2) {
            return -1
        } else if (year2 > year1) {
            return 1
        }
        return 0
    }

    private fun String.parseInt(): Int {
        try {
            return this.toInt()
        } catch (e: Throwable) {
            Log.e(SortMoviesByYear::class.java.simpleName, "Cannot parse: ${e.message}")
        }
        return 0
    }
}