package com.chuck.tests.intro

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chuck.ChuckApplication
import com.chuck.R
import com.chuck.di.DaggerTestApplicationComponent
import com.chuck.intro.IntroActivity
import com.chuck.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RandomJokeTest {

    @get:Rule
    var activityRule: ActivityTestRule<IntroActivity> = ActivityTestRule(IntroActivity::class.java)

    private lateinit var mockWebServer: MockWebServer
    private lateinit var app: ChuckApplication

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        app = instrumentation.targetContext.applicationContext as ChuckApplication
        mockWebServer = DaggerTestApplicationComponent.factory().create(app).getMockWebServer()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, IntroActivity::class.java
        )

        activityRule.launchActivity(intent)
    }

    @After
    fun teadDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun jokeDisplayed() {

        val response: String =
            InstrumentationRegistry.getInstrumentation().context.assets.open("joke.json")
                .bufferedReader().use {
                    it.readText()
                }
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(response))

        onView(withId(R.id.random_joke_button)).check(matches(isDisplayed()))
        onView(withId(R.id.custom_name_button)).check(matches(isDisplayed()))
        onView(withId(R.id.joke_list_button)).check(matches(isDisplayed()))

        onView(withId(R.id.random_joke_button)).perform(click())
        onView(withId(android.R.id.message)).check(matches(withText(JOKE_TEST)))
        onView(withText(DONE)).perform(click())
    }

    companion object {
        const val JOKE_TEST = "Chuck Norris joke text"
        const val DONE = "Done"
    }
}