package com.harshil.trendingnews.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.data.ArticleRemoteKey

@Database(entities = [Article::class, ArticleRemoteKey::class], version = 1)
@TypeConverters(RoomTypeConvertor::class)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object {
        @Volatile
        private var instance: NewsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, NewsDatabase::class.java, "news.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}