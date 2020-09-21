package com.tincho5588.reddittopsreader.data.repository

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.data.model.Post

interface TopsRepository {
    fun getTops(): LiveData<List<Post>>

    fun refreshPosts(doneCallback: (() -> Unit)? = null)

    fun markAsSeen(post: Post)

    fun dismiss(post: Post)

    fun dismissAll()
}