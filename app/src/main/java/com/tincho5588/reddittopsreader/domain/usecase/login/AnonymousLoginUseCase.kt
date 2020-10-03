package com.tincho5588.reddittopsreader.domain.usecase.login

import com.tincho5588.reddittopsreader.domain.datasource.login.LoginDataSource
import com.tincho5588.reddittopsreader.domain.usecase.Resource
import com.tincho5588.reddittopsreader.domain.usecase.Status
import com.tincho5588.reddittopsreader.domain.usecase.SyncUseCase

/**
 * As a user I want to anonymously log in to Reddit using my Device ID, and obtain a valid,
 * non-expired access token hash to get authorization against the Reddit OAuth2 API.
 */
class AnonymousLoginUseCase(
    val loginDataSource: LoginDataSource
): SyncUseCase<AnonymousLoginUseCase.RequestValues, String>() {

    override fun executeUseCase(requestValues: RequestValues?): Resource<String> {
        requestValues!!
        val tokenResponse = loginDataSource.getDeviceAccessToken(requestValues.deviceId)

        // Assert the response from the datasource is SUCCESS, that the token is not null, and that it is not
        // expired.
        val isSuccess = (tokenResponse.status == Status.SUCCESS) && (!(tokenResponse.data?.expired ?: true))

        return Resource(if(isSuccess) Status.SUCCESS else Status.ERROR, tokenResponse.data?.access_token, "")
    }

    class RequestValues(val deviceId: String): SyncUseCase.RequestValues
}