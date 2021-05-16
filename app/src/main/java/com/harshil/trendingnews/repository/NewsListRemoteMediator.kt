package com.harshil.trendingnews.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.data.ArticleRemoteKey
import com.harshil.trendingnews.network.TrendingNewsApiInterface
import com.harshil.trendingnews.room.NewsDao
import com.harshil.trendingnews.utils.NETWORK_PAGE_SIZE
import com.harshil.trendingnews.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class NewsListRemoteMediator(
    private val newsDao: NewsDao,
    private val trendingNewsApiInterface: TrendingNewsApiInterface,
    private val initialPage: Int = STARTING_PAGE_INDEX
) : RemoteMediator<Int, Article>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Article>
    ): MediatorResult {
        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val response =
                trendingNewsApiInterface.getAllTopHeadlines(
                    page = page,
                    pageSize = NETWORK_PAGE_SIZE
                )
            if (response.isSuccessful) {
                val endOfPagination = response.body()?.articles?.size!! < state.config.pageSize

                response.body()?.let {

                    // flush our data
                    if (loadType == LoadType.REFRESH) {
                        newsDao.deleteAllTopHeadlines()
                        newsDao.deleteAllRemoteKeys()
                    }


                    val prev = if (page == initialPage) null else page - 1
                    val next = if (endOfPagination) null else page + 1

                    val list = response.body()?.articles?.map { article ->
                        ArticleRemoteKey(article.title, prev, next)
                    }


                    // make list of remote keys
                    if (list != null) {
                        newsDao.insertAllRemoteKeys(list)
                    }

                    // insert to the room
                    newsDao.insertTopHeadlines(it.articles.orEmpty())
                }
                MediatorResult.Success(endOfPagination)
            } else {
                MediatorResult.Success(endOfPaginationReached = true)
            }
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Article>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.next?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.next
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prev ?: return MediatorResult.Success(
                    endOfPaginationReached = false
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): ArticleRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.title?.let { repoId ->
                newsDao.getRemoteKey(repoId)
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Article>): ArticleRemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { article -> newsDao.getRemoteKey(article.title) }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Article>): ArticleRemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { article -> newsDao.getRemoteKey(article.title) }
    }
}