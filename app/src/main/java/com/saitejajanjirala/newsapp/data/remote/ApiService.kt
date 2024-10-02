package com.saitejajanjirala.newsapp.data.remote

import com.saitejajanjirala.newsapp.data.models.HeadLine
import com.saitejajanjirala.newsapp.util.Keys
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET(Keys.HEAD_LINES)
    suspend fun getHeadLines(@Query("category") category: String) : HeadLine?
}