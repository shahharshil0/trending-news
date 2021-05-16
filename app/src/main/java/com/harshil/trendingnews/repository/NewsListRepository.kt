package com.harshil.trendingnews.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.network.TrendingNewsApiInterface
import com.harshil.trendingnews.room.NewsDatabase
import com.harshil.trendingnews.utils.NETWORK_PAGE_SIZE
import com.harshil.trendingnews.utils.STARTING_PAGE_INDEX
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListRepository @Inject constructor(
    private val newsDatabase: NewsDatabase,
    private val trendingNewsApiInterface: TrendingNewsApiInterface
) {
    /**
     * fetch top news headlines
     */
    @ExperimentalPagingApi
    fun fetchTopHeadlines(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = 1,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            remoteMediator = NewsListRemoteMediator(
                newsDao = newsDatabase.newsDao(),
                trendingNewsApiInterface = trendingNewsApiInterface,
                initialPage = STARTING_PAGE_INDEX
            )
        ) {
            newsDatabase.newsDao().getAllTopHeadlines()
        }.flow
    }
}