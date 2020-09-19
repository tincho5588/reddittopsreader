package com.tincho5588.reddittopsreader.login.provider

import android.content.Context
import android.os.StrictMode
import android.os.SystemClock
import androidx.preference.PreferenceManager
import com.tincho5588.reddittopsreader.login.model.AccessToken
import com.tincho5588.reddittopsreader.login.retrofit.service.AccessTokenService
import com.tincho5588.reddittopsreader.util.Constants.DEVICE_ID_PREF_KEY
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_APP_ID
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_GRANT_TYPE
import okhttp3.Credentials
import java.util.*

class AccessTokenProviderImpl(
    private val context: Context,
    private val accessTokenService: AccessTokenService
) :
    AccessTokenProvider {
    init {
        // Allow networking on the main thread
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override val token: String
        get() {
            if (!::realToken.isInitialized || realToken.isExpired()) {
                refreshToken()
            }
            return realToken.access_token
        }

    private val deviceId: String by lazy {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        var uuid: String = sharedPreferences.getString(DEVICE_ID_PREF_KEY, "") ?: ""
        if (uuid.isEmpty()) {
            uuid = UUID.randomUUID().toString()
            sharedPreferences.edit().putString(DEVICE_ID_PREF_KEY, uuid).apply()
        }
        uuid
    }

    private lateinit var realToken: AccessToken

    private fun refreshToken() {
        val basic = Credentials.basic(REDDIT_APP_ID, "")
        val call = accessTokenService.getAccessToken(
            basic,
            REDDIT_GRANT_TYPE,
            deviceId
        )

        val response = call.execute()
        if (response.isSuccessful) {
            val accessTokenResponse = response.body()!!
            realToken = accessTokenResponse
            realToken.retrieveTime = SystemClock.elapsedRealtime()
            return
        }
    }
}