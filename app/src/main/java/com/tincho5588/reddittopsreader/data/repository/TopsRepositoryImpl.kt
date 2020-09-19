package com.tincho5588.reddittopsreader.data.repository

import androidx.lifecycle.LiveData
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.room.dao.PostDao
import com.tincho5588.reddittopsreader.data.model.TopsList
import com.tincho5588.reddittopsreader.data.retrofit.service.TopsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopsRepositoryImpl(
    private val topsService: TopsService,
    private val postDao: PostDao
) : TopsRepository {
    override fun getTops(): LiveData<List<Post>> {
        // Returns a LiveData object directly from the database.
        return postDao.load()
    }

    override fun refreshPosts() {
        val call = topsService.getTops(50)
        call.enqueue(object : Callback<TopsList> {
            override fun onFailure(call: Call<TopsList>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<TopsList>, response: Response<TopsList>) {
                val topsList = response.body()!!

                GlobalScope.launch(Dispatchers.IO) {
                    val retrievedPosts = ArrayList<Post>()
                    val dismissedAndSeenPost: List<Post> = postDao.loadSeenPosts()
                    topsList.data.children.forEach {
                        for (dismissedAndSeen in dismissedAndSeenPost) {
                            if (dismissedAndSeen.id == it.data.id) {
                                it.data.seen = dismissedAndSeen.seen
                                break
                            }
                        }
                        retrievedPosts.add(it.data)
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
