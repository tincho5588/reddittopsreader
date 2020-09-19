package com.tincho5588.reddittopsreader.login.model

import android.os.SystemClock

data class AccessToken(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val scope: String,
    var retrieveTime: Long = 0
) {
    fun isExpired(): Boolean {
        return (retrieveTime + expires_in) <= SystemClock.elapsedRealtime()
    }
}