package com.tincho5588.reddittopsreader.data.datasource.remote.post.service

import com.tincho5588.reddittopsreader.data.datasource.remote.post.response.TopsListApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TopsService {
    @GET("/r/all/top")
    fun getTops(@Query("limit") count: Int): Call<TopsListApiResponse>
}