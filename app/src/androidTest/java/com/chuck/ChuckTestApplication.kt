package com.chuck

import com.chuck.di.DaggerApplicationComponent
import com.chuck.di.DaggerTestApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class ChuckTestApplication : ChuckApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerTestApplicationComponent.factory().create(applicationContext)
    }
}