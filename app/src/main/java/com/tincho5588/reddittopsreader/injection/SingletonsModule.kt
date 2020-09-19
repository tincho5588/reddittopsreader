package com.tincho5588.reddittopsreader.injection

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.tincho5588.reddittopsreader.data.repository.TopsRepository
import com.tincho5588.reddittopsreader.data.repository.TopsRepositoryImpl
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import com.tincho5588.reddittopsreader.data.room.database.PostsDatabase
import com.tincho5588.reddittopsreader.login.provider.AccessTokenProvider
import com.tincho5588.reddittopsreader.login.provider.AccessTokenProviderImpl
import com.tincho5588.reddittopsreader.login.retrofit.interceptor.InsertHeadersRequestInterceptor
import com.tincho5588.reddittopsreader.login.retrofit.service.AccessTokenService
import com.tincho5588.reddittopsreader.util.Constants.POSTS_DATABASE_NAME
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SingletonsModule {
    @Provides
    @Singleton
    fun provideAccessTokenProvider(
        @ApplicationContext context: Context,
        accessTokenService: AccessTokenService
    ): AccessTokenProvider {
        return AccessTokenProviderImpl(context, accessTokenService)
    }

    @Provides
    @Singleton
    fun provideTopsRepository(@ApplicationContext context: Context, topsService: TopsService, postDao: PostDao): TopsRepository {
        return TopsRepositoryImpl(context, topsService, postDao)
    }

    @Provides
    @Singleton
    fun providePostsDatabase(@ApplicationContext context: Context): PostsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PostsDatabase::class.java,
            POSTS_DATABASE_NAME
        ).build()
    }

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
