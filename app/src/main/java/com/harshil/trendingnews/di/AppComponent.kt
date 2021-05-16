package com.harshil.trendingnews.di

import android.app.Application
import com.harshil.trendingnews.base.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ViewModelModule::class, DatabaseModule::class])
interface AppComponent {

    fun getApplication(): Application
    fun inject(baseFragment: BaseFragment)
}