package com.tincho5588.reddittopsreader.data.repository

import com.tincho5588.reddittopsreader.data.datasource.remote.login.RemoteLoginDataSource
import com.tincho5588.reddittopsreader.domain.model.login.AccessToken
import com.tincho5588.reddittopsreader.domain.usecase.Resource

class LoginRepositoryImpl(
    val remoteLoginDataSource: RemoteLoginDataSource
) : LoginRepository {
    override fun getDeviceAccessToken(deviceId: String): Resource<AccessToken> {
        return remoteLoginDataSource.getDeviceAccessToken(deviceId)
    }
}