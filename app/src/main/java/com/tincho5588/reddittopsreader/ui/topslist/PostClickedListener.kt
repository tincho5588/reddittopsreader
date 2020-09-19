package com.tincho5588.reddittopsreader.ui.topslist

import com.tincho5588.reddittopsreader.data.model.Post

interface PostClickedListener {
    fun onPostItemClicked(post: Post)
}