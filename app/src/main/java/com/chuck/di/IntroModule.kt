package com.chuck.di

import com.chuck.intro.IntroFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class IntroModule {

    @ContributesAndroidInjector()
    internal abstract fun introFragment(): IntroFragment
}