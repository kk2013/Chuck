package com.chuck.di

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.platform.app.InstrumentationRegistry
import com.chuck.ChuckApplication
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import javax.inject.Inject

class IdlingResourceRule @Inject constructor(idlingResource: IdlingResource): TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
//                IdlingRegistry.getInstance().register(idlingResource)
                base.evaluate()
//                IdlingRegistry.getInstance().unregister(idlingResource)
            }
        }
    }
}