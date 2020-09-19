package com.tincho5588.reddittopsreader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class TopsList(
    val kind: String,
    val data: Data
)

data class Data(
    val modhash: String,
    val dist: Int,
    val children: List<Children>,
    val after: String,
    val before: String
)

data class Children(
    val kind: String,
    val data: Post
)

@Entity
data class Post(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnail_height: Int,
    val thumbnail_width: Int,
    val thumbnail: String,
    val author: String,
    val num_comments: Int,
    val permalink: String,
    val created_utc: Int,
    val ups: Int,
    val subreddit_name_prefixed: String,
    var seen: Boolean = false,
    var dismissed: Boolean = false
)