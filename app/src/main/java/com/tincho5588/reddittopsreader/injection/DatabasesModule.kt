package com.tincho5588.reddittopsreader.injection

import android.content.Context
import androidx.room.Room
import com.tincho5588.reddittopsreader.data.room.database.PostsDatabase
import com.tincho5588.reddittopsreader.util.Constants
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
            Constants.POSTS_DATABASE_NAME
        ).build()
    }
}