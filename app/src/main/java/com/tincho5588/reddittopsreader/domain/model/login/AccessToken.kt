package com.tincho5588.reddittopsreader.domain.model.login

import android.os.SystemClock

data class AccessToken(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val scope: String,
    val retrievedTime: Long
) {
    val expired: Boolean
        get() {
            return (retrievedTime + expires_in) <= SystemClock.elapsedRealtime()
        }
}