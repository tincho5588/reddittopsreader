package com.tincho5588.reddittopsreader.data.datasource.remote.login.service

import com.tincho5588.reddittopsreader.manager.login.LoginManager
import okhttp3.Interceptor
import okhttp3.Response

class InsertHeadersRequestInterceptor(private val loginManager: LoginManager) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${loginManager.getDeviceAccessToken()}")
                .addHeader("User-agent", "RedditTopReader/0.1 by tincho5588")
                .build()
        )
    }
}