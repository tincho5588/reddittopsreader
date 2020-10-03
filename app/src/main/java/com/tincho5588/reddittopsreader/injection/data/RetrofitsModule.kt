package com.tincho5588.reddittopsreader.injection.data

import com.google.gson.GsonBuilder
import com.tincho5588.reddittopsreader.data.datasource.remote.login.service.InsertHeadersRequestInterceptor
import com.tincho5588.reddittopsreader.manager.login.LoginManager
import com.tincho5588.reddittopsreader.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitsModule {
    @Provides
    @Singleton
    @Named("LoginApi")
    fun provideLoginApiRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl(Constants.REDDIT_AUTHENTICATION_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("DataApi")
    fun provideDataApiRetrofit(loginManager: LoginManager): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(
            InsertHeadersRequestInterceptor(
                loginManager.getDeviceAccessToken()
            )
        )
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(Constants.REDDIT_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}