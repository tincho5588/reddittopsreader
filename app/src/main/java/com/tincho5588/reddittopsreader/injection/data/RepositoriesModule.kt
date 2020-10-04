package com.tincho5588.reddittopsreader.injection.data

import com.tincho5588.reddittopsreader.data.datasource.local.LocalPostsDataSource
import com.tincho5588.reddittopsreader.data.datasource.remote.login.RemoteLoginDataSource
import com.tincho5588.reddittopsreader.data.datasource.remote.post.RemotePostsDataSource
import com.tincho5588.reddittopsreader.data.repository.LoginRepository
import com.tincho5588.reddittopsreader.data.repository.LoginRepositoryImpl
import com.tincho5588.reddittopsreader.data.repository.PostsRepository
import com.tincho5588.reddittopsreader.data.repository.PostsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {
    @Provides
    @Singleton
    fun providePostsRepository(
        localPostsDataSource: LocalPostsDataSource,
        remotePostsDataSource: RemotePostsDataSource
    ): PostsRepository {
        return PostsRepositoryImpl(localPostsDataSource, remotePostsDataSource)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(remoteLoginDataSource: RemoteLoginDataSource): LoginRepository {
        return LoginRepositoryImpl(remoteLoginDataSource)
    }
}