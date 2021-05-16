package com.harshil.trendingnews.di

import android.app.Application
import com.harshil.trendingnews.room.NewsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesNewsListDatabase(context: Application): NewsDatabase =
        NewsDatabase.invoke(context)
}