package com.tincho5588.reddittopsreader.data.datasource.remote.login

import android.content.Context
import android.os.StrictMode
import android.os.SystemClock
import androidx.annotation.VisibleForTesting
import com.tincho5588.reddittopsreader.R
import com.tincho5588.reddittopsreader.data.datasource.remote.login.service.AccessTokenService
import com.tincho5588.reddittopsreader.domain.model.login.AccessToken
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import com.tincho5588.reddittopsreader.domain.usecase.Status.SUCCESS
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_APP_ID
import com.tincho5588.reddittopsreader.util.Constants.REDDIT_GRANT_TYPE
import okhttp3.Credentials

class RemoteLoginDataSourceImpl(
    private val context: Context,
    private val accessTokenService: AccessTokenService
) :
    RemoteLoginDataSource {

    init {
        // Allow networking on the main thread
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    @VisibleForTesting
    internal lateinit var tokenResource: Resource<AccessToken>

    override fun getDeviceAccessToken(deviceId: String): Resource<AccessToken> {
        if (!::tokenResource.isInitialized ||
            this.tokenResource.status != SUCCESS ||
            tokenResource.data?.expired != false
        ) {
            refreshToken(deviceId)
        }
        return tokenResource
    }

    private fun refreshToken(deviceId: String) {
        val basic = Credentials.basic(REDDIT_APP_ID, "")
        val call = accessTokenService.getAccessToken(
            basic,
            REDDIT_GRANT_TYPE,
            deviceId
        )

        val response = call.execute()
        if (response.isSuccessful) {
            val accessTokenResponse = response.body()!!
            // Android Bug Workaround: using the SystemClock in a constructor does not work.
            accessTokenResponse.createdTime = SystemClock.elapsedRealtime() / 1000
            tokenResource = Resource.success(accessTokenResponse)
        } else {
            tokenResource =
                Resource.error(context.getString(R.string.failed_login, response.code()), null)
        }
    }
}