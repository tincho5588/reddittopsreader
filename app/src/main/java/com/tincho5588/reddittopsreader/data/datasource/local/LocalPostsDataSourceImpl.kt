package com.tincho5588.reddittopsreader.data.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tincho5588.reddittopsreader.data.datasource.local.dao.PostDao
import com.tincho5588.reddittopsreader.data.datasource.local.entity.PostRoomEntity
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocalPostsDataSourceImpl(
    val postsDao: PostDao,
    val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : LocalPostsDataSource {
    override fun getNonDismissedTopPosts(
        amount: Int
    ): LiveData<Resource<List<Post>>> {
        val ret = MutableLiveData<Resource<List<Post>>>().also { ret ->
            ret.value = Resource.loading(emptyList())
        }

        GlobalScope.launch(dispatcher) {
            postsDao.loadNotDismissed(amount).observeForever {
                ret.value = Resource.success(it)
            }
        }
        return ret
    }

    override fun refreshTopPosts(amount: Int): LiveData<Resource<Void>> {
        return MutableLiveData(Resource.error("Not supported on non-repository datasources", 405, null))
    }

    override fun getPost(id: String): LiveData<Resource<Post>> {
        val ret = MutableLiveData<Resource<Post>>().also { ret ->
            ret.value = Resource.loading(null)
        }

        GlobalScope.launch(dispatcher) {
            ret.value = Resource.success(postsDao.loadPost(id))
        }
        return ret
    }

    override fun dismissPost(post: Post): LiveData<Resource<Void>> {
        val ret = MutableLiveData<Resource<Void>>().also { ret ->
            ret.value = Resource.loading(null)
        }

        GlobalScope.launch(dispatcher) {
            postsDao.dismiss(post.id)
            ret.value = Resource.success(null)
        }
        return ret
    }

    override fun markPostAsSeen(post: Post): LiveData<Resource<Void>> {
        val ret = MutableLiveData<Resource<Void>>().also { ret ->
            ret.value = Resource.loading(null)
        }

        GlobalScope.launch(dispatcher) {
            postsDao.markAsSeen(post.id)
            ret.value = Resource.success(null)
        }
        return ret
    }

    override fun save(posts: List<Post>) {
        val out: MutableList<PostRoomEntity> = ArrayList()
        posts.forEach { post ->
            out.add(PostRoomEntity.fromPost(post))
        }

        GlobalScope.launch(dispatcher) {
            postsDao.save(out)
        }
    }
}