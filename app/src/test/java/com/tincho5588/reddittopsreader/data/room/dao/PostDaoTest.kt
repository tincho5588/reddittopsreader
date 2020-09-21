package com.tincho5588.reddittopsreader.data.room.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.room.database.PostsDatabase
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPost
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class PostDaoTest {
    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * SUT
     */
    private lateinit var postDao: PostDao

    private lateinit var db: PostsDatabase

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(
            context, PostsDatabase::class.java).allowMainThreadQueries().build()
        postDao = db.postDao()
        MockitoAnnotations.openMocks(this)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertListOfPosts() {
        val posts = listOf(provideNotSeenNotDismissedPost(), provideNotSeenNotDismissedPost())

        postDao.insert(posts)

        assertEquals(posts, postDao.loadAll())
    }

    @Test
    fun testLoadNotDismissedPostsOnlyReturnedNonDismissed() {
        val dismissed = provideNotSeenNotDismissedPost()
        dismissed.dismissed = true
        val nonDismissed = provideNotSeenNotDismissedPost()

        postDao.insert(listOf(dismissed, nonDismissed))
        val result = postDao.loadNotDismissed().observeBlocking()

        result!!
        assertTrue(result.contains(nonDismissed))
        assertFalse(result.contains(dismissed))
    }

    @Test
    fun testLoadNotDismissedOnlyReturnsFiftyResults() {
        val postsList = ArrayList<Post>()
        for(i in 1..55) {
            postsList.add(provideNotSeenNotDismissedPost())
        }

        postDao.insert(postsList)
        val result = postDao.loadNotDismissed().observeBlocking()

        result!!
        assertTrue(result.size == 50)

    }

    @Test
    fun testLoadSeenPosts() {
        val seen = provideNotSeenNotDismissedPost()
        seen.seen = true
        val notSeen = provideNotSeenNotDismissedPost()

        postDao.insert(listOf(seen, notSeen))

        val result = postDao.loadSeenPosts()

        assertTrue(result.contains(seen))
        assertFalse(result.contains(notSeen))
    }

    @Test
    fun testDismissPost() {
        val post = provideNotSeenNotDismissedPost()

        postDao.insert(listOf(post))
        postDao.dismiss(post.id)

        val result = postDao.loadAll()[0]
        assertEquals(post.id, result.id)
        assertTrue(result.dismissed)
    }

    @Test
    fun testDismissAllPosts() {
        val post1 = provideNotSeenNotDismissedPost()
        val post2 = provideNotSeenNotDismissedPost()

        postDao.insert(listOf(post1, post2))
        postDao.dismissAll()

        val result = postDao.loadAll()
        result.forEach { post ->
            assertTrue(post.dismissed)
        }
    }

    @Test
    fun testMarkAsSeenPost() {
        val post = provideNotSeenNotDismissedPost()

        postDao.insert(listOf(post))
        postDao.markAsSeen(post.id)

        val result = postDao.loadAll()[0]
        assertEquals(post.id, result.id)
        assertTrue(result.seen)
    }

    private fun <T> LiveData<T>.observeBlocking(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}