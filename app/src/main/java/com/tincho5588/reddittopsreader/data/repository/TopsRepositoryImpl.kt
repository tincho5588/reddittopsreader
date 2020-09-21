package com.tincho5588.reddittopsreader.data.repository

import android.content.Context
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.model.TopsListResponse
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import com.tincho5588.reddittopsreader.util.Constants.POSTS_TO_SHOW
import com.tincho5588.reddittopsreader.util.Utils.isNetworkAvailable
import com.tincho5588.reddittopsreader.util.Utils.showNetworkUnavailableToast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopsRepositoryImpl(
    private val context: Context,
    private val topsService: TopsService,
    private val postDao: PostDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TopsRepository {
    override fun getTops(): LiveData<List<Post>> {
        // Returns a LiveData object directly from the database.
        return postDao.loadNotDismissed()
    }

    override fun refreshPosts() {
        if (!isNetworkAvailable(context)) {
            showNetworkUnavailableToast(context)
            return
        }

        val call = topsService.getTops(POSTS_TO_SHOW)
        call.enqueue(object : Callback<TopsListResponse> {
            override fun onFailure(call: Call<TopsListResponse>, t: Throwable) {
                Toast.makeText(context, context.getString(R.string.failed_to_retrieve), LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TopsListResponse>, response: Response<TopsListResponse>) {
                GlobalScope.launch(dispatcher) {
                    postDao.insert(unwrapResponse(response.body()!!))
                }
            }
        })
    }

    private fun unwrapResponse(response: TopsListResponse): List<Post> {
        val retrievedPosts = ArrayList<Post>()
        val seenPosts: List<Post> = postDao.loadSeenPosts()
        response.data.children.forEach { fetchedPost ->
            retainSeenState(fetchedPost.data, seenPosts)
            retrievedPosts.add(fetchedPost.data)
        }
        return retrievedPosts
    }

    @VisibleForTesting
    internal fun retainSeenState(fetchedPost: Post, seenPosts: List<Post>) {
        fetchedPost.seen =
            seenPosts.firstOrNull { fetchedPost.id == it.id } != null
    }

    override fun markAsSeen(post: Post) {
        GlobalScope.launch(dispatcher) {
            postDao.markAsSeen(post.id)
        }
    }

    override fun dismiss(post: Post) {
        GlobalScope.launch(dispatcher) {
            postDao.dismiss(post.id)
        }
    }

    override fun dismissAll() {
        GlobalScope.launch(dispatcher) {
            postDao.dismissAll()
        }
    }
}
