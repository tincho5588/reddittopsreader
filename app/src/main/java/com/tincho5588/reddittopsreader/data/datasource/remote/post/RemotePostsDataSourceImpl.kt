package com.tincho5588.reddittopsreader.data.datasource.remote.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tincho5588.reddittopsreader.data.datasource.remote.post.response.TopsListApiResponse
import com.tincho5588.reddittopsreader.data.datasource.remote.post.service.TopsService
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemotePostsDataSourceImpl(
    val topsService: TopsService
) : RemotePostsDataSource {

    override fun getNonDismissedTopPosts(
        amount: Int
    ): LiveData<Resource<List<Post>>> {

        val ret = MutableLiveData<Resource<List<Post>>>().also { ret ->
            ret.value = Resource.loading(emptyList())
        }

        val call = topsService.getTops(amount)
        call.enqueue(object : Callback<TopsListApiResponse> {
            override fun onFailure(call: Call<TopsListApiResponse>, t: Throwable) {
                ret.value =
                    Resource.error(
                        "Failed to generate request to retrieve posts",
                        0,
                        emptyList()
                    )
            }

            override fun onResponse(
                call: Call<TopsListApiResponse>,
                response: Response<TopsListApiResponse>
            ) {
                if (response.isSuccessful)
                    ret.value = Resource.success(unwrapResponse(response.body()!!))
                else
                    ret.value = Resource.error(
                        "Failed to retrieve posts from remote",
                        response.code(),
                        emptyList()
                    )
            }
        })

        return ret
    }

    override fun refreshTopPosts(amount: Int): LiveData<Resource<Void>> {
        return MutableLiveData<Resource<Void>>(
            Resource.error(
                "Not supported on non-repository datasources",
                405,
                null
            )
        )
    }

    override fun getPost(id: String): LiveData<Resource<Post>> {
        return MutableLiveData<Resource<Post>>(
            Resource.error(
                "Operation not supported on Remote DataSource",
                405,
                null
            )
        )
    }

    override fun dismissPost(post: Post): LiveData<Resource<Void>> {
        return MutableLiveData<Resource<Void>>(
            Resource.error(
                "Operation not supported on Remote DataSource",
                405,
                null
            )
        )
    }

    override fun markPostAsSeen(post: Post): LiveData<Resource<Void>> {
        return MutableLiveData<Resource<Void>>(
            Resource.error(
                "Operation not supported on Remote DataSource",
                405,
                null
            )
        )
    }

    private fun unwrapResponse(response: TopsListApiResponse): List<Post> {
        val retrievedPosts = ArrayList<Post>()
        response.data.children.forEach { postResponse ->
            retrievedPosts.add(postResponse.data)
        }
        return retrievedPosts
    }
}