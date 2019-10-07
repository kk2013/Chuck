package com.chuck.di

import androidx.lifecycle.ViewModel
import com.chuck.jokes.JokesFragment
import com.chuck.list.JokesViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class JokesModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun jokesFragment(): JokesFragment

    @Binds
    @IntoMap
    @ViewModelKey(JokesViewModel::class)
    abstract fun bindViewModel(viewModel: JokesViewModel): ViewModel
}