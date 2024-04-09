package com.arsfutura.imdb.data.network

import com.arsfutura.imdb.data.network.NetworkConstants.API_KEY
import com.arsfutura.imdb.data.network.NetworkConstants.API_KEY_VALUE
import com.arsfutura.imdb.data.network.NetworkConstants.TYPE_PARAM
import com.arsfutura.imdb.data.network.NetworkConstants.TYPE_PARAM_VALUE
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class NetworkRequestInterceptor : Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY, API_KEY_VALUE)
            .addQueryParameter(TYPE_PARAM, TYPE_PARAM_VALUE)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder()
            .url(url)

        val request: Request = requestBuilder.build()
        return chain.proceed(request)
    }
}