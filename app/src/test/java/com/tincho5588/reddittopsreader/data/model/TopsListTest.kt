package com.tincho5588.reddittopsreader.data.model

import android.os.Parcel
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
        post = Post(
            "1",
            "Sometittle",
            "SomeThumbnail",
            "SomeAuthor",
            3,
            123456789,
            987654321,
            "SomeSubreddit",
            "SomeUrl",
            true,
            true
        )
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