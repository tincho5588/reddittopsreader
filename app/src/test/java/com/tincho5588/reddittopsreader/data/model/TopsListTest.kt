package com.tincho5588.reddittopsreader.data.model

import android.os.Parcel
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPost
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class TopsListTest {
    private lateinit var post: Post

    @Before
    fun setup() {
        post = provideNotSeenNotDismissedPost()
    }

    @Test
    fun testSerializationAndDeserializationConsistency() {
        val parcel = Parcel.obtain()
        post.writeToParcel(parcel, post.describeContents())
        parcel.setDataPosition(0)

        val createdFromParcel: Post = Post.CREATOR.createFromParcel(parcel)
        assertEquals(createdFromParcel, post)
    }
}