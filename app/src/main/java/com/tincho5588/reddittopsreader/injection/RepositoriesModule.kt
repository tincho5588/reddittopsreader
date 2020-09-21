package com.tincho5588.reddittopsreader.injection

import android.content.Context
import com.tincho5588.reddittopsreader.data.repository.TopsRepository
import com.tincho5588.reddittopsreader.data.repository.TopsRepositoryImpl
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {
    @Provides
    @Singleton
    fun provideTopsRepository(@ApplicationContext context: Context, topsService: TopsService, postDao: PostDao): TopsRepository {
        return TopsRepositoryImpl(context, topsService, postDao)
    }
}