package com.chuck.tests.joke

import android.content.Intent
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chuck.ChuckApplication
import com.chuck.ChuckTestApplication
import com.chuck.R
import com.chuck.di.DaggerTestApplicationComponent
import com.chuck.intro.IntroActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomNameJokeTest {

    @get:Rule
    var activityRule: ActivityTestRule<IntroActivity> = ActivityTestRule(IntroActivity::class.java)
//    @get: Rule
//    var idlingResourceRule: IdlingResourceRule = IdlingResourceRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var app: ChuckApplication

    @Before
    fun setUp() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        app = instrumentation.targetContext.applicationContext as ChuckTestApplication
        mockWebServer = DaggerTestApplicationComponent.factory().create(app).getMockWebServer()

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, IntroActivity::class.java
        )

        activityRule.launchActivity(intent)
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

        onView(withId(R.id.custom_name_button)).perform(click())

        onView(withId(R.id.name_label))
            .check(matches(withText("Enter a first and last name to be used in the joke")))
        onView(withId(R.id.name_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.done_button)).check(matches(isDisplayed()))

        onView(withId(R.id.done_button)).perform(click())

        onView(withText("Please check that you have entered a first and last name")).check(
            matches(
                isDisplayed()
            )
        )
        onView(withText("DONE")).perform(click())

        onView(withId(R.id.name_edit_text)).perform(clearText(), typeText("John Smith"))
        closeSoftKeyboard()
        onView(withId(R.id.done_button)).perform(click())

        Thread.sleep(5000)
        onView(withId(android.R.id.message)).check(matches(withText(JOKE_TEST)))
        onView(withText("DONE")).perform(click())
    }

    companion object {
        const val JOKE_TEST = "Chuck Norris joke text"
    }
}