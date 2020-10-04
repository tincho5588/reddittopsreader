package com.tincho5588.reddittopsreader.domain.model.login

import android.os.SystemClock

open class AccessToken(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
    val scope: String,
    var createdTime: Long
) {
    val expired: Boolean
        get() {
            return ((SystemClock.elapsedRealtime() / 1000) - createdTime) >= expires_in
        }
}