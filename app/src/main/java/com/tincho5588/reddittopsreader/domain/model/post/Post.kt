package com.tincho5588.reddittopsreader.domain.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey

// ToDo: Cheating here. Using the domain entity as database entity. Not good, but we avoid a lot troubles with this
@Entity
data class Post(
    @PrimaryKey val id: String,
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