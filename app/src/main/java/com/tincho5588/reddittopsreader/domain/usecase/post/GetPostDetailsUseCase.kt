package com.tincho5588.reddittopsreader.domain.usecase.post

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.domain.datasource.post.PostsDataSource
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource

/**
 * As a user, I want to see an specific post details.
 */
class GetPostDetailsUseCase(
    private val postsDataSource: PostsDataSource
) : AsyncUseCase<GetPostDetailsUseCase.RequestValues, Post>() {

    override fun executeUseCase(requestValues: RequestValues?): LiveData<Resource<Post>> {
        requestValues!!
        return postsDataSource.getPost(requestValues.id)
    }

    class RequestValues(val id: String) : AsyncUseCase.RequestValues
}