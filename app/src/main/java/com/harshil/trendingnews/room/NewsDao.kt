package com.harshil.trendingnews.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.data.ArticleRemoteKey

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopHeadlines(list: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopHeadlines(article: Article)

    @Query("SELECT * FROM Article ")
    fun getAllTopHeadlines(): PagingSource<Int, Article>

    @Query("DELETE FROM Article")
    suspend fun deleteAllTopHeadlines()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(list: List<ArticleRemoteKey>)

    @Query("SELECT * FROM ArticleRemoteKey WHERE id = :id")
    suspend fun getRemoteKey(id: String): ArticleRemoteKey?

    @Query("DELETE FROM ArticleRemoteKey")
    suspend fun deleteAllRemoteKeys()

}