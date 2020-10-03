package com.tincho5588.reddittopsreader.data.datasource.local

import com.tincho5588.reddittopsreader.domain.datasource.post.PostsDataSource
import com.tincho5588.reddittopsreader.domain.model.post.Post

interface LocalPostsDataSource: PostsDataSource {
    fun save(posts: List<Post>)
}