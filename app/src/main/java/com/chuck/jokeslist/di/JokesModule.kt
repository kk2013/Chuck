package com.chuck.jokeslist.di

import androidx.lifecycle.ViewModel
import com.chuck.di.ViewModelBuilder
import com.chuck.di.ViewModelKey
import com.chuck.jokeslist.JokesListFragment
import com.chuck.jokeslist.JokesListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class JokesModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun jokesFragment(): JokesListFragment

    @Binds
    @IntoMap
    @ViewModelKey(JokesListViewModel::class)
    abstract fun bindViewModel(viewModel: JokesListViewModel): ViewModel
}