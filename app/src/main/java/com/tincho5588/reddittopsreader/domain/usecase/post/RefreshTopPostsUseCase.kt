package com.tincho5588.reddittopsreader.domain.usecase.post

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.domain.datasource.post.PostsDataSource
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource

/**
 * As a user, I want to to refresh the posts cache (if there's any), so when
 * {@link GetTopPostsUseCase GetTopPostsUseCase.class} is executed, it returns up-to-date
 * data.
 * If the application has no cache policy, then this usecase must do nothing.
 */
class RefreshTopPostsUseCase(
    private val postsDataSource: PostsDataSource
) : AsyncUseCase<AsyncUseCase.RequestValues, Void>() {
    companion object {
        // ToDo: unify this with the GetTopPostsUseCase value
        const val POSTS_TO_SHOW = 50
    }

    override fun executeUseCase(requestValues: RequestValues?): LiveData<Resource<Void>> {
        return postsDataSource.refreshTopPosts(POSTS_TO_SHOW)
    }
}