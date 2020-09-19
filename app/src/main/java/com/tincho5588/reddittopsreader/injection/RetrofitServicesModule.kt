package com.tincho5588.reddittopsreader.injection

import com.google.gson.GsonBuilder
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.login.retrofit.service.AccessTokenService
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_AUTHENTICATION_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitServicesModule {
    @Provides
    fun provideTopsService(retrofit: Retrofit): TopsService {
        return retrofit.create(
            TopsService::class.java
        )
    }

    @Provides
    fun provideAccessTokenService(): AccessTokenService {
        // Not using the injected retrofit here
        // For the AccessTokenService we want a different top level URL and no interceptor
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(REDDIT_AUTHENTICATION_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(
            AccessTokenService::class.java
        )
    }
}