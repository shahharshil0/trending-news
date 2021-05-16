package com.harshil.trendingnews.di

import android.app.Application
import com.ashokvarma.gander.GanderInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.harshil.trendingnews.BuildConfig
import com.harshil.trendingnews.network.AuthHeaderInterceptor
import com.harshil.trendingnews.network.TrendingNewsApiInterface
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val API_ENDPOINT = BuildConfig.BASE_URL
        private const val READ_TIMEOUT_SEC = 15L
    }

    @Provides
    fun provideGanderInterceptor(application: Application) =
        GanderInterceptor(application).showNotification(true)

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authHeaderInterceptor: AuthHeaderInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        ganderInterceptor: GanderInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authHeaderInterceptor)
            .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .callTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(httpLoggingInterceptor)
                .addInterceptor(ganderInterceptor)
        }

        return builder.build()
    }

    @Provides
    fun provideGson(
    ): Gson {
        val gsonBuilder = GsonBuilder().enableComplexMapKeySerialization()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideTrendingNewsApi(retrofit: Retrofit): TrendingNewsApiInterface {
        return retrofit.create(TrendingNewsApiInterface::class.java)
    }
}
