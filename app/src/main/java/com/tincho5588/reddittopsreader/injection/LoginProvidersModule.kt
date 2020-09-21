package com.tincho5588.reddittopsreader.injection

import android.content.Context
import com.tincho5588.reddittopsreader.login.provider.AccessTokenProvider
import com.tincho5588.reddittopsreader.login.provider.AccessTokenProviderImpl
import com.tincho5588.reddittopsreader.login.retrofit.service.AccessTokenService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginProvidersModule {
    @Provides
    @Singleton
    fun provideAccessTokenProvider(
        @ApplicationContext context: Context,
        accessTokenService: AccessTokenService
    ): AccessTokenProvider {
        return AccessTokenProviderImpl(context, accessTokenService)
    }
}