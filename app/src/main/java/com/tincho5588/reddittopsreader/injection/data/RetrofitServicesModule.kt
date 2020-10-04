package com.tincho5588.reddittopsreader.injection.data

import com.tincho5588.reddittopsreader.data.datasource.remote.login.service.AccessTokenService
import com.tincho5588.reddittopsreader.data.datasource.remote.post.service.TopsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitServicesModule {
    @Provides
    fun provideTopsService(@Named("DataApi") retrofit: Retrofit): TopsService {
        return retrofit.create(
            TopsService::class.java
        )
    }

    @Provides
    fun provideAccessTokenService(@Named("LoginApi") retrofit: Retrofit): AccessTokenService {
        return retrofit.create(
            AccessTokenService::class.java
        )
    }
}