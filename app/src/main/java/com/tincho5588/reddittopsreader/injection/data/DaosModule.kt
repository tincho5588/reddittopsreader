package com.tincho5588.reddittopsreader.injection.data

import com.tincho5588.reddittopsreader.data.datasource.local.dao.PostDao
import com.tincho5588.reddittopsreader.data.datasource.local.database.PostsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object DaosModule {
    @Provides
    fun providePostDao(postsDatabase: PostsDatabase): PostDao {
        return postsDatabase.postDao()
    }
}