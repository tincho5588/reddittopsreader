package com.tincho5588.reddittopsreader.login.retrofit.service

import com.tincho5588.reddittopsreader.login.model.AccessToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AccessTokenService {
    @FormUrlEncoded
    @POST("/api/v1/access_token")
    fun getAccessToken(
        @Header("Authorization") h1: String,
        @Field("grant_type") grant_type: String,
        @Field("device_id") device_id: String,
        @Field("duration") duration: String = "permanent"
    ): Call<AccessToken>
}