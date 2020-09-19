package com.tincho5588.reddittopsreader.login.retrofit.interceptor

import com.tincho5588.reddittopsreader.login.provider.AccessTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class InsertHeadersRequestInterceptor(private val accessTokenProvider: AccessTokenProvider) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${accessTokenProvider.token}")
                .addHeader("User-agent", "RedditTopReader/0.1 by tincho5588")
                .build()
        )
    }
}