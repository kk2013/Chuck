package com.chuck.tests.jokeslist

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.chuck.ChuckApplication
import com.chuck.di.DaggerTestApplicationComponent
import com.chuck.intro.IntroActivity
import com.chuck.robots.IntroRobot
import com.chuck.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JokesListTest {

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
    fun tearDown() {
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

        IntroRobot().checkIntroScreen()
            .selectJokesList()
            .checkJokes()
    }
}
