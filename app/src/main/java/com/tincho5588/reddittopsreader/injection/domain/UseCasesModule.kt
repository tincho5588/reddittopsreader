package com.tincho5588.reddittopsreader.injection.domain

import com.tincho5588.reddittopsreader.data.repository.LoginRepository
import com.tincho5588.reddittopsreader.data.repository.PostsRepository
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.SyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.login.AnonymousLoginUseCase
import com.tincho5588.reddittopsreader.domain.usecase.post.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Named

@Module
@InstallIn(ApplicationComponent::class)
object UseCasesModule {
    @Provides
    @Named("DismissPostUseCase")
    fun provideDismissPostUseCase(postsRepository: PostsRepository): AsyncUseCase<DismissPostUseCase.RequestValues, Void> {
        return DismissPostUseCase(postsRepository)
    }

    @Provides
    @Named("GetPostDetailsUseCase")
    fun provideGetPostDetailsUseCase(postsRepository: PostsRepository): AsyncUseCase<GetPostDetailsUseCase.RequestValues, Post> {
        return GetPostDetailsUseCase(postsRepository)
    }

    @Provides
    @Named("GetTopPostsUseCase")
    fun provideGetTopPostUseCase(postsRepository: PostsRepository): AsyncUseCase<AsyncUseCase.RequestValues, List<Post>> {
        return GetTopPostsUseCase(postsRepository)
    }

    @Provides
    @Named("MarkPostAsSeenUseCase")
    fun provideMarkPostAsSeenUseCase(postsRepository: PostsRepository): AsyncUseCase<MarkPostAsSeenUseCase.RequestValues, Void> {
        return MarkPostAsSeenUseCase(postsRepository)
    }

    @Provides
    @Named("RefreshTopPostsUseCase")
    fun provideRefreshTopPostsUseCase(postsRepository: PostsRepository): AsyncUseCase<AsyncUseCase.RequestValues, Void> {
        return RefreshTopPostsUseCase(postsRepository)
    }

    @Provides
    @Named("AnonymousLoginUseCase")
    fun provideAnonymousLoginUseCase(loginRepository: LoginRepository): SyncUseCase<AnonymousLoginUseCase.RequestValues, String> {
        return AnonymousLoginUseCase(loginRepository)
    }
}