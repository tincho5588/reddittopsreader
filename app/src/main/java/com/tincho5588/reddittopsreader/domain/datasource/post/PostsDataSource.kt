package com.tincho5588.reddittopsreader.domain.datasource.post

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.Resource

interface PostsDataSource {
    fun getNonDismissedTopPosts(amount: Int): LiveData<Resource<List<Post>>>

    fun refreshTopPosts(amount: Int): LiveData<Resource<Void>>

    fun getPost(id: String): LiveData<Resource<Post>>

    fun dismissPost(post: Post): LiveData<Resource<Void>>

    fun markPostAsSeen(post: Post): LiveData<Resource<Void>>
}