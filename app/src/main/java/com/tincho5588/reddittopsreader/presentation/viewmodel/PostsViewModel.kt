package com.tincho5588.reddittopsreader.presentation.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tincho5588.reddittopsreader.domain.model.post.Post
import com.tincho5588.reddittopsreader.domain.usecase.AsyncUseCase
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import com.tincho5588.reddittopsreader.domain.usecase.post.DismissPostUseCase
import com.tincho5588.reddittopsreader.domain.usecase.post.GetPostDetailsUseCase
import com.tincho5588.reddittopsreader.domain.usecase.post.MarkPostAsSeenUseCase
import javax.inject.Named

class PostsViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @Named("DismissPostUseCase") val dismissPostUseCase: AsyncUseCase<DismissPostUseCase.RequestValues, Void>,
    @Named("GetPostDetailsUseCase") val getPostDetailsUseCase: AsyncUseCase<GetPostDetailsUseCase.RequestValues, Post>,
    @Named("GetTopPostsUseCase") val getTopPostsUseCase: AsyncUseCase<AsyncUseCase.RequestValues, List<Post>>,
    @Named("MarkPostAsSeenUseCase") val markPostAsSeenUseCase: AsyncUseCase<MarkPostAsSeenUseCase.RequestValues, Void>,
    @Named("RefreshTopPostsUseCase") val refreshTopPostsUseCase: AsyncUseCase<AsyncUseCase.RequestValues, Void>
) : ViewModel() {
    val posts: LiveData<Resource<List<Post>>> = getTopPostsUseCase.run()

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
        return refreshTopPostsUseCase.run()
    }
}