package com.harshil.trendingnews.di

import android.app.Application
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class, DatabaseModule::class])
interface AppComponent {

    fun getApplication(): Application

}