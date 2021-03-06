package com.tincho5588.reddittopsreader.injection.data

import com.tincho5588.reddittopsreader.data.datasource.local.LocalPostsDataSource
import com.tincho5588.reddittopsreader.data.datasource.local.LocalPostsDataSourceImpl
import com.tincho5588.reddittopsreader.data.datasource.local.dao.PostDao
import com.tincho5588.reddittopsreader.data.datasource.remote.login.RemoteLoginDataSource
import com.tincho5588.reddittopsreader.data.datasource.remote.login.RemoteLoginDataSourceImpl
import com.tincho5588.reddittopsreader.data.datasource.remote.login.service.AccessTokenService
import com.tincho5588.reddittopsreader.data.datasource.remote.post.RemotePostsDataSource
import com.tincho5588.reddittopsreader.data.datasource.remote.post.RemotePostsDataSourceImpl
import com.tincho5588.reddittopsreader.data.datasource.remote.post.service.TopsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object DataSourcesModule {
    @Provides
    fun provideLocalPostsDataSource(postsDao: PostDao): LocalPostsDataSource {
        return LocalPostsDataSourceImpl(postsDao)
    }

    @Provides
    fun provideRemotePostsDataSource(
        topsService: TopsService
    ): RemotePostsDataSource {
        return RemotePostsDataSourceImpl(topsService)
    }

    @Provides
    fun provideRemoteLoginDataSource(
        accessTokenService: AccessTokenService
    ): RemoteLoginDataSource {
        return RemoteLoginDataSourceImpl(accessTokenService)
    }
}