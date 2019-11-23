package com.chuck.di

import android.content.Context
import com.chuck.ChuckApplication
import com.chuck.intro.di.IntroModule
import com.chuck.joke.di.JokeModule
import com.chuck.jokeslist.di.JokesModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, NetworkModule::class, IntroModule::class, JokeModule::class, JokesModule::class])
interface ApplicationComponent : AndroidInjector<ChuckApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}