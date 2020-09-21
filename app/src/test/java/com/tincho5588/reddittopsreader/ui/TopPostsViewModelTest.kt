package com.tincho5588.reddittopsreader.ui

import androidx.lifecycle.SavedStateHandle
import com.tincho5588.reddittopsreader.data.model.PostsProvider.provideNotSeenNotDismissedPost
import com.tincho5588.reddittopsreader.data.repository.TopsRepository
import com.tincho5588.reddittopsreader.ui.topslist.TopPostsViewModel
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
    private lateinit var topPostsViewModel: TopPostsViewModel

    @Mock lateinit var topsRepository: TopsRepository
    @Mock lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        topPostsViewModel = TopPostsViewModel(topsRepository, savedStateHandle)
    }

    @Test
    fun testLiveDataIsPopularedOnCreation() {
        verify(topsRepository, times(1)).getTops()
    }

    @Test
    fun testRefreshRequestCallsRepository() {
        topPostsViewModel.refreshPosts()

        verify(topsRepository, times(1)).refreshPosts()
    }

    @Test
    fun testMarkAsSeenRequestCallsRepository() {
        val post = provideNotSeenNotDismissedPost()
        topPostsViewModel.markAsSeen(post)

        verify(topsRepository, times(1)).markAsSeen(post)
    }

    @Test
    fun testDismissRequestCallsRepository() {
        val post = provideNotSeenNotDismissedPost()
        topPostsViewModel.dismiss(post)

        verify(topsRepository, times(1)).dismiss(post)
    }

    @Test
    fun testDismissAllRequestCallsRepository() {
        topPostsViewModel.dismissAll()

        verify(topsRepository, times(1)).dismissAll()
    }
}