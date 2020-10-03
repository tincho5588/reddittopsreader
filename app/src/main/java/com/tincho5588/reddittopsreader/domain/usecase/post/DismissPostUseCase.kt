package com.tincho5588.reddittopsreader.domain.usecase.post

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.domain.datasource.post.PostsDataSource
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource

/**
 * As a user, I want to dismiss a post I'm not interested in.
 */
class DismissPostUseCase(
    private val postsDataSource: PostsDataSource
) : AsyncUseCase<DismissPostUseCase.RequestValues, Void>() {

    override fun executeUseCase(requestValues: RequestValues?): LiveData<Resource<Void>> {
        requestValues!!
        return postsDataSource.dismissPost(requestValues.post)
    }

    class RequestValues(val post: Post) : AsyncUseCase.RequestValues
}