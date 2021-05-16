package com.harshil.trendingnews.repository

import com.harshil.trendingnews.network.TrendingNewsApiInterface
import javax.inject.Inject

class NewsListRepository @Inject constructor(
    private val newsApiInterface: TrendingNewsApiInterface
)