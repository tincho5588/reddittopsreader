package com.tincho5588.reddittopsreader.presentation.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import com.tincho5588.reddittopsreader.domain.usecase.Status
import com.tincho5588.reddittopsreader.domain.usecase.post.DismissPostUseCase
import com.tincho5588.reddittopsreader.domain.usecase.post.GetPostDetailsUseCase
import com.tincho5588.reddittopsreader.domain.usecase.post.MarkPostAsSeenUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

class PostsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context,
    @Named("DismissPostUseCase") val dismissPostUseCase: AsyncUseCase<DismissPostUseCase.RequestValues, Void>,
    @Named("GetPostDetailsUseCase") val getPostDetailsUseCase: AsyncUseCase<GetPostDetailsUseCase.RequestValues, Post>,
    @Named("GetTopPostsUseCase") val getTopPostsUseCase: AsyncUseCase<AsyncUseCase.RequestValues, List<Post>>,
    @Named("MarkPostAsSeenUseCase") val markPostAsSeenUseCase: AsyncUseCase<MarkPostAsSeenUseCase.RequestValues, Void>,
    @Named("RefreshTopPostsUseCase") val refreshTopPostsUseCase: AsyncUseCase<AsyncUseCase.RequestValues, Void>
) : ViewModel() {

    fun getTopPosts(): LiveData<Resource<List<Post>>> {
        val ret = getTopPostsUseCase.run()
        ret.observeForever {
            if (it.status == Status.ERROR) {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_retrieve_errorcode, it.httpResponseCode),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return ret
    }

    fun dismissPost(post: Post): LiveData<Resource<Void>> {
        dismissPostUseCase.requestValues = DismissPostUseCase.RequestValues(post)

        return dismissPostUseCase.run()
    }

    fun getPostDetails(postId: String): LiveData<Resource<Post>> {
        getPostDetailsUseCase.requestValues = GetPostDetailsUseCase.RequestValues(postId)

        return getPostDetailsUseCase.run()
    }

    fun markPostAsSeen(post: Post): LiveData<Resource<Void>> {
        markPostAsSeenUseCase.requestValues = MarkPostAsSeenUseCase.RequestValues(post)

        return markPostAsSeenUseCase.run()
    }

    fun refreshTopPosts(): LiveData<Resource<Void>> {
        val ret = refreshTopPostsUseCase.run()
        ret.observeForever {
            if (it.status == Status.ERROR) {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_retrieve_errorcode, it.httpResponseCode),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return ret
    }
}