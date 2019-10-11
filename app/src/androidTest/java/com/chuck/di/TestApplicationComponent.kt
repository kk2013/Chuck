package com.chuck.di

import android.content.Context
import com.chuck.ChuckApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, TestNetworkModule::class, IntroModule::class, JokeModule::class, JokesModule::class])

interface TestApplicationComponent : ApplicationComponent {

    fun getMockWebServer(): MockWebServer

    override fun inject(app: ChuckApplication)
    /*    @Component.Builder
        interface Builder {
            @BindsInstance
            fun create(app: Application):Builder

            fun build(): TestApplicationComponent
        }*/
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestApplicationComponent
    }
}