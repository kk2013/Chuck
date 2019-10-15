package com.chuck.di

import com.chuck.BuildConfig
import com.chuck.api.ChuckJokeService
import com.chuck.utils.LiveDataCallAdapterFactory
import com.jakewharton.espresso.OkHttp3IdlingResource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class TestNetworkModule {

    private val PORT = 8080
    private val BASE_URL = "http://127.0.0.1:$PORT/"

    @Provides
    @Singleton
    fun provideMockServer(): MockWebServer {
        var mockWebServer: MockWebServer? = null
        val thread = Thread(Runnable {
            mockWebServer = MockWebServer()
            mockWebServer?.start(PORT)
        })
        thread.start()
        thread.join()
        return mockWebServer ?: throw NullPointerException()
    }

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(mockWebServer: MockWebServer): String {
        var url = ""
        val t = Thread(Runnable {
            url = mockWebServer.url(BASE_URL).toString()
        })
        t.start()
        t.join()
        return url
    }

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.SECONDS)
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