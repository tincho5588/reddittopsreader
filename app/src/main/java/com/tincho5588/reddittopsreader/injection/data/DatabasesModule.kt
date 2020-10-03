package com.tincho5588.reddittopsreader.injection.data

import android.content.Context
import androidx.room.Room
import com.tincho5588.reddittopsreader.data.datasource.local.database.PostsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabasesModule {
    @Provides
    @Singleton
    fun providePostsDatabase(@ApplicationContext context: Context): PostsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PostsDatabase::class.java,
            "reddit_database"
        ).build()
    }
}