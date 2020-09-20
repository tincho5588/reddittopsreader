package com.tincho5588.reddittopsreader.data.utils

import com.tincho5588.reddittopsreader.data.model.Post

object PostsProvider {
    var instanceNumber = 0

    fun provideNotSeenNotDismissedPost(): Post {
        return provideNotSeenNotDismissedPostWithId(instanceNumber++)
    }

    fun provideNotSeenNotDismissedPostWithId(id: Int): Post {
        return Post(
            id.toString(),
            "Sometittle $id.toString()",
            "SomeThumbnail $id.toString()\"",
            "SomeAuthor $id.toString()",
            3 * id,
            123L * id,
            123 * id,
            "SomeSubreddit  $id.toString()",
            "SomeUrl  $id.toString()"
        )
    }
}