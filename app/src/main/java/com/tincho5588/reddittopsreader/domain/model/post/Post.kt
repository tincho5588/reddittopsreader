package com.tincho5588.reddittopsreader.domain.model.post

open class Post(
    val id: String,
    val title: String,
    val thumbnail: String,
    val author: String,
    val num_comments: Int,
    val created_utc: Long,
    val ups: Int,
    val subreddit_name_prefixed: String,
    val url: String,
    var seen: Boolean,
    var dismissed: Boolean
)