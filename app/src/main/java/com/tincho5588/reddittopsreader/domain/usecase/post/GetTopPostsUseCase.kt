package com.tincho5588.reddittopsreader.domain.usecase.post

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.domain.datasource.post.PostsDataSource
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource

/**
 * As a user I want to be able to see the top 50 posts in Reddit. The source of this posts can be
 * local, remote, or both.
 * If the implementation contains both local and remote data sources, then a cache strategy is
 * mandatory.
 */
open class GetTopPostsUseCase(
    private val postsDataSource: PostsDataSource
): AsyncUseCase<AsyncUseCase.RequestValues, List<Post>>() {
    companion object {
        // ToDo: unify this with the RefreshTopPostsUseCase value
        const val POSTS_TO_SHOW = 50
    }

    override fun executeUseCase(requestValues: RequestValues?): LiveData<Resource<List<Post>>> {
        return postsDataSource.getNonDismissedTopPosts(POSTS_TO_SHOW)
    }
}