package com.tincho5588.reddittopsreader.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tincho5588.reddittopsreader.data.datasource.local.LocalPostsDataSource
import com.tincho5588.reddittopsreader.data.datasource.remote.post.RemotePostsDataSource
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import com.tincho5588.reddittopsreader.domain.usecase.Status

class PostsRepositoryImpl(
    private val localPostsDataSource: LocalPostsDataSource,
    private val remotePostsDataSource: RemotePostsDataSource,
) : PostsRepository {
    var isDirty = true

    override fun getNonDismissedTopPosts(
        amount: Int
    ): LiveData<Resource<List<Post>>> {
        if (isDirty) {
            isDirty = false
            refreshTopPosts(amount)
        }

        return localPostsDataSource.getNonDismissedTopPosts(amount)
    }

    override fun refreshTopPosts(
        amount: Int
    ): LiveData<Resource<Void>> {
        val ret = MutableLiveData<Resource<Void>>(Resource.loading(null))

        remotePostsDataSource.getNonDismissedTopPosts(amount).observeForever { resource ->
            ret.value = Resource(resource.status, null, resource.message)

            if (resource.status == Status.SUCCESS) {
                localPostsDataSource.save(resource.data!!)
            }
        }

        return ret
    }

    override fun getPost(id: String): LiveData<Resource<Post>> {
        return localPostsDataSource.getPost(id)
    }

    override fun dismissPost(post: Post): LiveData<Resource<Void>> {
        return localPostsDataSource.dismissPost(post)
    }

    override fun markPostAsSeen(post: Post): LiveData<Resource<Void>> {
        return localPostsDataSource.markPostAsSeen(post)
    }

}
