package com.tincho5588.reddittopsreader.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.tincho5588.reddittopsreader.data.model.Post

interface TopsRepository {
    fun getTops(): LiveData<PagedList<Post>>

    fun fetchForItems(after: String, amount: Int, doneCallback: (() -> Unit)?)

    fun markAsSeen(post: Post)

    fun dismiss(post: Post)
}