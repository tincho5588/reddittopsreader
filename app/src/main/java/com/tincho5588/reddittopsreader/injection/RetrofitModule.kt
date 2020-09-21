package com.tincho5588.reddittopsreader.injection

import com.google.gson.GsonBuilder
import com.tincho5588.reddittopsreader.login.provider.AccessTokenProvider
import com.tincho5588.reddittopsreader.login.retrofit.interceptor.InsertHeadersRequestInterceptor
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(accessTokenProvider: AccessTokenProvider): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(InsertHeadersRequestInterceptor(accessTokenProvider))
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(REDDIT_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
