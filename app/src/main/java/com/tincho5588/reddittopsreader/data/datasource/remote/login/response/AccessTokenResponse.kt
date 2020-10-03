package com.tincho5588.reddittopsreader.data.datasource.remote.login.response

import android.os.SystemClock
import com.tincho5588.reddittopsreader.domain.model.login.AccessToken

data class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val scope: String,
) {
    private val retrievedTime = SystemClock.elapsedRealtime()

    val expired: Boolean
        get() {
            return (retrievedTime + expires_in) <= SystemClock.elapsedRealtime()
        }

    fun toAccessToken(): AccessToken {
        return AccessToken(
            access_token,
            token_type,
            expires_in,
            scope,
            retrievedTime
        )
    }
}