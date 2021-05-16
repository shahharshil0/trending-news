package com.harshil.trendingnews

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.ashokvarma.gander.Gander
import com.ashokvarma.gander.imdb.GanderIMDB
import com.harshil.trendingnews.di.AppComponent
import com.harshil.trendingnews.di.AppModule
import com.harshil.trendingnews.di.DaggerAppComponent

class TrendingNewsApplication : Application() {
    companion object {
        lateinit var INSTANCE: TrendingNewsApplication
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Gander.setGanderStorage(GanderIMDB.getInstance())
        }

        INSTANCE = this
    }
}

val AppCompatActivity.app: TrendingNewsApplication
    get() = application as TrendingNewsApplication