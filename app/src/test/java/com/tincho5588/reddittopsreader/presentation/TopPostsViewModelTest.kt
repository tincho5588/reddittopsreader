package com.tincho5588.reddittopsreader.presentation

import androidx.lifecycle.SavedStateHandle
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPost
import com.tincho5588.reddittopsreader.data.repository.PostsRepository
import com.tincho5588.reddittopsreader.presentation.viewmodel.PostsViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class TopPostsViewModelTest {
    /**
     * SUT
     */
    private lateinit var topPostsViewModel: PostsViewModel

    @Mock lateinit var postsRepository: PostsRepository
    @Mock lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        topPostsViewModel =
            PostsViewModel(
                postsRepository,
                savedStateHandle
            )
    }

    @Test
    fun testLiveDataIsPopularedOnCreation() {
        verify(postsRepository, times(1)).getTops()
    }

    @Test
    fun testRefreshRequestCallsRepository() {
        topPostsViewModel.refreshPosts()

        verify(postsRepository, times(1)).refreshPosts()
    }

    @Test
    fun testMarkAsSeenRequestCallsRepository() {
        val post = provideNotSeenNotDismissedPost()
        topPostsViewModel.markAsSeen(post)

        verify(postsRepository, times(1)).markAsSeen(post)
    }

    @Test
    fun testDismissRequestCallsRepository() {
        val post = provideNotSeenNotDismissedPost()
        topPostsViewModel.dismiss(post)

        verify(postsRepository, times(1)).dismiss(post)
    }

    @Test
    fun testDismissAllRequestCallsRepository() {
        topPostsViewModel.dismissAll()

        verify(postsRepository, times(1)).dismissAll()
    }
}