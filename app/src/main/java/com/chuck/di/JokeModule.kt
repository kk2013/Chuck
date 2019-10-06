package com.chuck.di

import androidx.lifecycle.ViewModel
import com.chuck.intro.IntroFragment
import com.chuck.joke.JokeFragment
import com.chuck.joke.JokeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class JokeModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun jokeFragment(): JokeFragment

    @Binds
    @IntoMap
    @ViewModelKey(JokeViewModel::class)
    abstract fun bindViewModel(viewmodel: JokeViewModel): ViewModel
}