package com.chuck.tests.jokes

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasChildCount
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

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class JokesTest {

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
    fun jokesDisplayed() {

        val response: String =
            InstrumentationRegistry.getInstrumentation().context.assets.open("jokes.json")
                .bufferedReader().use {
                    it.readText()
                }
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(response))

        onView(withId(R.id.random_joke_button)).check(matches(isDisplayed()))
        onView(withId(R.id.custom_name_button)).check(matches(isDisplayed()))
        onView(withId(R.id.joke_list_button)).check(matches(isDisplayed()))

        onView(withId(R.id.joke_list_button)).perform(click())

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
//        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(1)))
        onView(withText(JOKE_1_TEXT)).check(matches(isDisplayed()))
        onView(withText(JOKE_4_TEXT)).check(matches(isDisplayed()))
    }

    companion object {
        const val JOKE_1_TEXT = "Chuck Norris joke 1"
        const val JOKE_4_TEXT = "Chuck Norris joke 4"
    }
}
