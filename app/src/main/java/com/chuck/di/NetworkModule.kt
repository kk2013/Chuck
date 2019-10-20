package com.chuck.di

import com.chuck.BuildConfig
import com.chuck.api.ChuckJokeService
import com.chuck.utils.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



@Module
class NetworkModule {

    private val BASE_URL = "http://api.icndb.com"

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun providesChuckService(retrofit: Retrofit): ChuckJokeService =
        retrofit.create(ChuckJokeService::class.java)
}