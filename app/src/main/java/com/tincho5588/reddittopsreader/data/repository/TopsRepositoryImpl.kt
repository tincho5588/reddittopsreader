package com.tincho5588.reddittopsreader.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.model.TopsList
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import com.tincho5588.reddittopsreader.util.Constants.POSTS_TO_SHOW
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
    override fun getTops(): LiveData<List<Post>> {
        // Returns a LiveData object directly from the database.
        return postDao.load()
    }

    override fun refreshPosts() {
        if (!isNetworkAvailable(context)) {
            showNetworkUnavailableToast(context)
            return
        }

        val call = topsService.getTops(POSTS_TO_SHOW)
        call.enqueue(object : Callback<TopsList> {
            override fun onFailure(call: Call<TopsList>, t: Throwable) {
                Log.d(TopsRepositoryImpl::class.simpleName, "Failed to retrieve posts from Reddit")
            }

            override fun onResponse(call: Call<TopsList>, response: Response<TopsList>) {
                val topsList = response.body()!!

                GlobalScope.launch(Dispatchers.IO) {
                    val retrievedPosts = ArrayList<Post>()
                    val dismissedAndSeenPost: List<Post> = postDao.loadSeenPosts()
                    topsList.data.children.forEach { fetchedPost ->
                        for (dismissedAndSeen in dismissedAndSeenPost) {
                            if (dismissedAndSeen.id == fetchedPost.data.id) {
                                fetchedPost.data.seen = dismissedAndSeen.seen
                                break
                            }
                        }
                        retrievedPosts.add(fetchedPost.data)
                    }
                    postDao.insert(retrievedPosts)
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
