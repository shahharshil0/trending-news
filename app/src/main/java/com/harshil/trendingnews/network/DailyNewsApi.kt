package com.harshil.trendingnews.network

import com.harshil.trendingnews.data.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendingNewsApiInterface {
    companion object {
        private const val TOP_HEADLINES_END_POINT = "top-headlines"
    }

    @GET(TOP_HEADLINES_END_POINT)
    suspend fun getAllTopHeadlines(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<NewsResponse>
}