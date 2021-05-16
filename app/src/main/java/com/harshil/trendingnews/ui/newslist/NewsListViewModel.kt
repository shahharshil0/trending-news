package com.harshil.trendingnews.ui.newslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.repository.NewsListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsListViewModel @Inject constructor(
    repository: NewsListRepository
) : ViewModel() {

    /**
     * fetch top headlines
     */
    @ExperimentalPagingApi
    val topHeadlines: Flow<PagingData<Article>> =
        repository.fetchTopHeadlines().cachedIn(viewModelScope)
}