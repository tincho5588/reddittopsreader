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

data class PostApiResponse(
    val id: String,
    val title: String,
    val thumbnail: String,
    val author: String,
    val num_comments: Int,
    val created_utc: Long,
    val ups: Int,
    val subreddit_name_prefixed: String,
    val url: String
) {
    fun toPost(): Post {
        return Post(
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
    }
}