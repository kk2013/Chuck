package com.chuck.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.chuck.R

class IntroRobot {

    fun checkIntroScreen() = apply {
        onView(withId(R.id.random_joke_button))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.custom_name_button))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.joke_list_button))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    fun selectRandomJoke() = apply {
        onView(withId(R.id.random_joke_button)).perform(click())
    }

    fun selectCustomNameJoke(): CustomJokeRobot {
        onView(withId(R.id.custom_name_button)).perform(click())
        return CustomJokeRobot()
    }

    fun selectJokesList(): JokesListRobot {
        onView(withId(R.id.joke_list_button)).perform(click())
        return JokesListRobot()
    }

    fun checkJokeText() = apply {
        onView(withId(android.R.id.message)).check(matches(withText(JOKE_TEST)))
    }

    fun closeDialog() = apply {
        onView(withText(DONE)).perform(click())
    }

    companion object {
        const val JOKE_TEST = "Chuck Norris joke text"
        const val DONE = "Done"
    }
}
