package com.tincho5588.reddittopsreader.data.retrofit.service

import com.tincho5588.reddittopsreader.data.model.TopsList
import retrofit2.Call
import retrofit2.http.*

interface TopsService {
    @GET("/r/all/top")
    fun getTops(@Query("limit") count: Int): Call<TopsList>
}