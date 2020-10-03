package com.tincho5588.reddittopsreader.domain.datasource.login

import com.tincho5588.reddittopsreader.domain.model.login.AccessToken
import com.tincho5588.reddittopsreader.domain.usecase.Resource

interface LoginDataSource {
    fun getDeviceAccessToken(deviceId: String): Resource<AccessToken>
}