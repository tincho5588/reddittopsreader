package com.tincho5588.reddittopsreader.data.datasource.remote.login.response

import android.os.SystemClock
import com.tincho5588.reddittopsreader.domain.model.login.AccessToken

class AccessTokenResponse(
    access_token: String,
    token_type: String,
    expires_in: Long,
    scope: String,
): AccessToken(
    access_token,
    token_type,
    expires_in,
    scope,
    0
)