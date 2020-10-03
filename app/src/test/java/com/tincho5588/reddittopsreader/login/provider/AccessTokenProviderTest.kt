package com.tincho5588.reddittopsreader.login.provider

import android.os.SystemClock
import androidx.preference.PreferenceManager
import androidx.test.core.app.ApplicationProvider
import com.tincho5588.reddittopsreader.data.datasource.remote.login.RemoteLoginDataSourceImpl
import com.tincho5588.reddittopsreader.data.datasource.remote.login.response.AccessTokenResponse
import com.tincho5588.reddittopsreader.data.datasource.remote.login.service.AccessTokenService
import com.tincho5588.reddittopsreader.util.Constants
import com.tincho5588.reddittopsreader.util.Constants.DEVICE_ID_PREF_KEY
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import okhttp3.Credentials
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Call
import retrofit2.Response

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class AccessTokenProviderTest {
    /**
     * SUT
     */
    lateinit var accessTokenProvider: RemoteLoginDataSourceImpl

    @Mock lateinit var accessTokenService: AccessTokenService
    @Mock private lateinit var call: Call<AccessTokenResponse>
    @Mock private lateinit var response: Response<AccessTokenResponse>

    private lateinit var nonExpiredToken: AccessTokenResponse
    private lateinit var basicAuthCredentials: String

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        nonExpiredToken =
            AccessTokenResponse(
                "mock_token",
                "",
                SystemClock.elapsedRealtime() + 100,
                ""
            )
        basicAuthCredentials = Credentials.basic(Constants.REDDIT_APP_ID, "")
        accessTokenProvider =
            RemoteLoginDataSourceImpl(
                ApplicationProvider.getApplicationContext(),
                accessTokenService
            )
    }

    @Test
    fun testInitialTokenIsRetrieved() {
        setupRefreshMocks()
        val result = accessTokenProvider.tokenResource

        assertEquals(nonExpiredToken.access_token, result)
        verifyTokenRetrievedFromServer()
    }

    @Test
    fun verifyExpiredTokenCausesTokenRetrievalFromServer() {
        setupRefreshMocks()
        accessTokenProvider.tokenResource =
            AccessTokenResponse(
                "mock_token",
                "",
                SystemClock.elapsedRealtime() - 100,
                ""
            )

        val result = accessTokenProvider.tokenResource

        assertEquals(nonExpiredToken.access_token, result)
        verifyTokenRetrievedFromServer()
    }

    @Test
    fun testExitingTokenIsReturnedIfNotExpired() {
        accessTokenProvider.tokenResource = nonExpiredToken

        assertEquals(nonExpiredToken.access_token, accessTokenProvider.tokenResource)
        verifyTokenNotRetrievedFromServer()
    }

    @Test
    fun testDeviceIdIsPersisted() {
        val id = accessTokenProvider.deviceId

        val preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        assertTrue(preferences.contains(DEVICE_ID_PREF_KEY))
        assertEquals(id, preferences.getString(DEVICE_ID_PREF_KEY, ""))
    }

    @Test
    fun testDeviceIdPullsFromPreferencesIfValueExists() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(ApplicationProvider.getApplicationContext())
        preferences.edit().putString(DEVICE_ID_PREF_KEY, "mock_id").commit()

        assertEquals("mock_id", accessTokenProvider.deviceId)
    }

    private fun verifyTokenRetrievedFromServer() {
        verify(accessTokenService, times(1)).getAccessToken(
            basicAuthCredentials,
            Constants.REDDIT_GRANT_TYPE,
            accessTokenProvider.deviceId
        )
        verify(call, times(1)).execute()
        verify(response, times(1)).body()
    }

    private fun verifyTokenNotRetrievedFromServer() {
        verify(accessTokenService, never()).getAccessToken(
            basicAuthCredentials,
            Constants.REDDIT_GRANT_TYPE,
            accessTokenProvider.deviceId
        )
        verify(call, never()).execute()
        verify(response, never()).body()
    }

    private fun setupRefreshMocks() {
        `when`(accessTokenService.getAccessToken(
            basicAuthCredentials,
            Constants.REDDIT_GRANT_TYPE,
            accessTokenProvider.deviceId
        )).thenReturn(call)
        `when`(call.execute()).thenReturn(response)
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(nonExpiredToken)
    }
}