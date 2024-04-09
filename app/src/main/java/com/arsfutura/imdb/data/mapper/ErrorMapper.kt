package com.arsfutura.imdb.data.mapper

import com.arsfutura.imdb.data.errors.ApiError

class ErrorMapper {

    fun parse(throwable: Throwable): String {
        try {
            if (throwable is ApiError) {
                return throwable.errorMessage
            }
        } catch (t: Throwable) {
            return ""
        }
        return ""
    }
}