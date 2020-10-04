package com.tincho5588.reddittopsreader.data.datasource.remote.post.response

import com.tincho5588.reddittopsreader.domain.model.post.Post

data class TopsListApiResponse(
    val kind: String,
    val data: DataApiResponse
)

data class DataApiResponse(
    val modhash: String,
    val dist: Int,
    val children: List<ChildrenApiResponse>,
    val after: String,
    val before: String
)

data class ChildrenApiResponse(
    val kind: String,
    val data: PostApiResponse
)

class PostApiResponse(
    id: String,
    title: String,
    thumbnail: String,
    author: String,
    num_comments: Int,
    created_utc: Long,
    ups: Int,
    subreddit_name_prefixed: String,
    url: String
): Post(
    id,
    title,
    thumbnail,
    author,
    num_comments,
    created_utc,
    ups,
    subreddit_name_prefixed,
    url,
    false,
    false
)