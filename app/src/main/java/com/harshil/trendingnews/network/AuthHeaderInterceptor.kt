package com.harshil.trendingnews.network

import com.harshil.trendingnews.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthHeaderInterceptor @Inject constructor() : Interceptor {

    companion object {
        private const val AUTH_HEADER_KEY = "apiKey"
        private const val COUNTRY_KEY = "country"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val originalHttpUrl: HttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(AUTH_HEADER_KEY, BuildConfig.API_KEY)
            .addQueryParameter(COUNTRY_KEY, BuildConfig.COUNTRY)
            .build()

        val requestBuilder: Request.Builder = original.newBuilder().url(url)
        return chain.proceed(requestBuilder.build())
    }
}