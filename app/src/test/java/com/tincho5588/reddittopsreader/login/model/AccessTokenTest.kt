package com.tincho5588.reddittopsreader.login.model

import android.os.SystemClock
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.Assert
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class AccessTokenTest {
    @Test
    fun testExpiredToken() {
        val expiredToken = AccessToken("", "", SystemClock.elapsedRealtime() - 100, "")
        assertTrue(expiredToken.expired)
    }

    @Test
    fun testNonExpiredToken() {
        val nonExpiredToken = AccessToken("", "", SystemClock.elapsedRealtime() + 100, "")
        Assert.assertFalse(nonExpiredToken.expired)
    }
}