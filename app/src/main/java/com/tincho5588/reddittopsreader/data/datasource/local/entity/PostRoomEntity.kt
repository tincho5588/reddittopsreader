package com.tincho5588.reddittopsreader.data.datasource.local.entity

import androidx.room.Entity
import com.tincho5588.reddittopsreader.domain.model.post.Post

// ToDo: Cheating here... Instead of mapping we do inheritance due to convenience
@Entity(primaryKeys = ["id"])
class PostRoomEntity constructor(
    id: String,
    title: String,
    thumbnail: String,
    author: String,
    num_comments: Int,
    created_utc: Long,
    ups: Int,
    subreddit_name_prefixed: String,
    url: String,
    seen: Boolean,
    dismissed: Boolean
) : Post(
    id,
    title,
    thumbnail,
    author,
    num_comments,
    created_utc,
    ups,
    subreddit_name_prefixed,
    url,
    seen,
    dismissed
) {
    companion object {
        fun fromPost(post: Post): PostRoomEntity {
            return PostRoomEntity(
                post.id,
                post.title,
                post.thumbnail,
                post.author,
                post.num_comments,
                post.created_utc,
                post.ups,
                post.subreddit_name_prefixed,
                post.url,
                post.seen,
                post.dismissed
            )
        }
    }
}