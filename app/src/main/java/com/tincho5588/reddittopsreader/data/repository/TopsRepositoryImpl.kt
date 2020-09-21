package com.tincho5588.reddittopsreader.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.model.TopsList
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import com.tincho5588.reddittopsreader.util.Constants.DATA_PAGE_SIZE
import com.tincho5588.reddittopsreader.util.Utils.isNetworkAvailable
import com.tincho5588.reddittopsreader.util.Utils.showNetworkUnavailableToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopsRepositoryImpl(
    private val context: Context,
    private val topsService: TopsService,
    private val postDao: PostDao
) : TopsRepository {
    override fun getTops(): LiveData<PagedList<Post>> {
        val config = PagedList.Config.Builder()
            .setPageSize(DATA_PAGE_SIZE)
            .setInitialLoadSizeHint(DATA_PAGE_SIZE * 2)
            .setEnablePlaceholders(true)
            .build()

        return LivePagedListBuilder(postDao.load(), config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Post>() {
                override fun onZeroItemsLoaded() {
                    fetchForItems("", DATA_PAGE_SIZE * 2) {}
                }

                override fun onItemAtEndLoaded(itemAtEnd: Post) {
                    fetchForItems(itemAtEnd.name, DATA_PAGE_SIZE) {}
                }
            })
            .build()
    }

    override fun fetchForItems(after: String, amount: Int, doneCallback: (() -> Unit)?) {
        if (!isNetworkAvailable(context)) {
            showNetworkUnavailableToast(context)
            doneCallback?.invoke()
            return
        }

        val call = topsService.getTops(amount, after)
        call.enqueue(object : Callback<TopsList> {
            override fun onFailure(call: Call<TopsList>, t: Throwable) {
                Log.d(TopsRepositoryImpl::class.simpleName, "Failed to retrieve posts from Reddit")
                doneCallback?.invoke()
            }

            override fun onResponse(call: Call<TopsList>, response: Response<TopsList>) {
                val topsList = response.body()!!

                GlobalScope.launch(Dispatchers.IO) {
                    val retrievedPosts = ArrayList<Post>()
                    val seenPosts: List<Post> = postDao.loadSeenAndDismissedPosts()
                    topsList.data.children.forEach { fetchedPost ->
                        seenPosts.firstOrNull { fetchedPost.data.id == it.id }?.let {
                            fetchedPost.data.seen = it.seen
                            fetchedPost.data.dismissed = it.dismissed
                        }
                        retrievedPosts.add(fetchedPost.data)
                    }
                    postDao.insert(retrievedPosts)
                    doneCallback?.invoke()
                }
            }
        })
    }

    override fun markAsSeen(post: Post) {
        GlobalScope.launch(Dispatchers.IO) {
            postDao.markAsSeen(post.id)
        }
    }

    override fun dismiss(post: Post) {
        GlobalScope.launch(Dispatchers.IO) {
            postDao.dismiss(post.id)
        }
    }
}
