package com.chuck.di

import android.app.Application
import android.content.Context
import com.chuck.ChuckApplication
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