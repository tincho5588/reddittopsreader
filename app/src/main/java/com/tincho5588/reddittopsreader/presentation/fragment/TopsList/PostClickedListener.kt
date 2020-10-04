package com.tincho5588.reddittopsreader.presentation.fragment.TopsList

import com.tincho5588.reddittopsreader.domain.model.post.Post

interface PostClickedListener {
    fun onPostItemClicked(post: Post)
}