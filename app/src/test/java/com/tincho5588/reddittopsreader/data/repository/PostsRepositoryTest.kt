package com.tincho5588.reddittopsreader.data.repository

import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.model.Children
import com.tincho5588.reddittopsreader.data.model.Data
import com.tincho5588.reddittopsreader.data.model.Post
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPost
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPostWithId
import com.tincho5588.reddittopsreader.data.model.TopsListResponse
import com.tincho5588.reddittopsreader.data.datasource.remote.post.service.TopsService
import com.tincho5588.reddittopsreader.data.datasource.local.dao.PostDao
import com.tincho5588.reddittopsreader.util.Constants.POSTS_TO_SHOW
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class PostsRepositoryTest {

    /**
     * SUT
     */
    private lateinit var topsRepository: PostsRepositoryImpl

    @Mock private lateinit var topsService: TopsService
    @Mock private lateinit var postDao: PostDao

    @Mock private lateinit var call: Call<TopsListResponse>
    @Mock private lateinit var response: Response<TopsListResponse>
    @Captor private lateinit var callCallbackCaptor: ArgumentCaptor<Callback<TopsListResponse>>

    private lateinit var post: Post

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        topsRepository = PostsRepositoryImpl(
            ApplicationProvider.getApplicationContext(),
            topsService,
            postDao,
            TestCoroutineDispatcher()
        )
        post = provideNotSeenNotDismissedPost()
    }

    @Test
    fun testGetTopsLoadsItemsFromDatabase() {
        topsRepository.getTops()

        verify(postDao, times(1)).loadNotDismissed()
    }

    @Test
    fun testTopsNotRefreshedWhenNoNetwork() {
        val context: Context = ApplicationProvider.getApplicationContext()
        val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Shadows.shadowOf(connectivityManager).setDefaultNetworkActive(false)

        topsRepository.refreshPosts()

        assert(ShadowToast.showedToast(context.getString(R.string.network_unavailable_message)))
        verify(postDao, never()).insert(anyList())
        verify(topsService, never()).getTops(anyInt())
    }

    @Test
    fun testFailedTopsRefreshQueryShowsToast() {
        val context: Context = ApplicationProvider.getApplicationContext()

        `when`(topsService.getTops(POSTS_TO_SHOW)).thenReturn(call)
        topsRepository.refreshPosts()

        verify(topsService, times(1)).getTops(POSTS_TO_SHOW)
        verify(call, times(1)).enqueue(callCallbackCaptor.capture())

        val callback = callCallbackCaptor.value
        callback.onFailure(call, Throwable())
        assert(ShadowToast.showedToast(context.getString(R.string.failed_to_retrieve)))
    }

    @Test
    fun testTopsRefreshQueriesRemoteAndSavesToDatabase() {
        val post1 = provideNotSeenNotDismissedPost()
        val post2 = provideNotSeenNotDismissedPost()
        val childrenList = listOf(Children("", post1), Children("", post2))
        `when`(topsService.getTops(POSTS_TO_SHOW)).thenReturn(call)
        `when`(response.body()).thenReturn(createResponseBody(childrenList))

        topsRepository.refreshPosts()

        verify(topsService, times(1)).getTops(POSTS_TO_SHOW)
        verify(call, times(1)).enqueue(callCallbackCaptor.capture())

        val callback = callCallbackCaptor.value
        callback.onResponse(call, response)
        verify(postDao, times(1)).insert(listOf(post1, post2))
    }

    @Test
    fun testRetainSeenState() {
        val post1 = provideNotSeenNotDismissedPost()
        val post2 = provideNotSeenNotDismissedPost()
        post1.seen = true
        post2.seen = true

        val fetchedPost = provideNotSeenNotDismissedPostWithId(post2.id.toInt())
        assert(!fetchedPost.seen)

        topsRepository.retainSeenState(fetchedPost, listOf(post1, post2))
        assert(fetchedPost.seen)
    }

    @Test
    fun testMarkAsSeenUpdatesDatabase() {
        topsRepository.markAsSeen(post)

        verify(postDao, times(1)).markAsSeen(post.id)
    }

    @Test
    fun testDismissUpdatesDatabase() {
        topsRepository.dismiss(post)

        verify(postDao, times(1)).dismiss(post.id)
    }

    @Test
    fun testDismissAllUpdatesDatabase() {
        topsRepository.dismissAll()

        verify(postDao, times(1)).dismissAll()
    }

    private fun createResponseBody(children: List<Children>): TopsListResponse {
        return TopsListResponse("",
            Data("",
                0,
                children,
                "",
                ""))
    }
}