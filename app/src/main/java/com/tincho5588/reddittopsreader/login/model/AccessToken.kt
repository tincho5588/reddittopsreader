package com.tincho5588.reddittopsreader.login.model

import android.os.SystemClock

data class AccessToken(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val scope: String,
) {
    private val retrieveTime = SystemClock.elapsedRealtime()

    val expired: Boolean
        get() {
            return (retrieveTime + expires_in) <= SystemClock.elapsedRealtime()
        }
}