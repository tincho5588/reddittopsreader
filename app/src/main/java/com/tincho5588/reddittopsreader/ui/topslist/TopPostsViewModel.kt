package com.tincho5588.reddittopsreader.ui.topslist

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.repository.TopsRepository

class TopPostsViewModel @ViewModelInject constructor(
    private val topsRepository: TopsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val posts: LiveData<List<Post>> = topsRepository.getTops()

    fun refreshPosts() {
        topsRepository.refreshPosts()
    }

    fun markAsSeen(post: Post) {
        topsRepository.markAsSeen(post)
    }

    fun dismiss(post: Post) {
        topsRepository.dismiss(post)
    }

    fun dismissAll() {
        posts.value?.forEach {
            topsRepository.dismiss(it)
        }
    }
}