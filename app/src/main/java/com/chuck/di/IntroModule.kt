package com.chuck.di

import androidx.lifecycle.ViewModel
import com.chuck.intro.IntroFragment
import com.chuck.intro.IntroViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class IntroModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])

    internal abstract fun introFragment(): IntroFragment

    @Binds
    @IntoMap
    @ViewModelKey(IntroViewModel::class)
    abstract fun bindViewModel(viewModel: IntroViewModel): ViewModel
}