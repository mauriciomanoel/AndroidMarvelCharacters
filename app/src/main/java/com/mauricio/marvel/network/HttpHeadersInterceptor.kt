package com.mauricio.marvel.network

import com.mauricio.marvel.utils.network.MarvelAuthenticationUtils
import okhttp3.Interceptor
import okhttp3.Response

class HttpHeadersInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .build()

        val url = request.url().newBuilder()
            .addQueryParameter("ts", MarvelAuthenticationUtils.getTimestamp())
            .addQueryParameter("apikey", MarvelAuthenticationUtils.apiKey)
            .addQueryParameter("hash", MarvelAuthenticationUtils.getHash())
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}