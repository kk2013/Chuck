package com.chuck.di

import android.content.Context
import com.chuck.ChuckApplication
import com.chuck.intro.di.IntroModule
import com.chuck.joke.di.JokeModule
import com.chuck.jokeslist.di.JokesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton
import okhttp3.mockwebserver.MockWebServer

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, TestNetworkModule::class, IntroModule::class, JokeModule::class, JokesModule::class])
interface TestApplicationComponent : ApplicationComponent {

    fun getMockWebServer(): MockWebServer

    override fun inject(app: ChuckApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestApplicationComponent
    }
}
