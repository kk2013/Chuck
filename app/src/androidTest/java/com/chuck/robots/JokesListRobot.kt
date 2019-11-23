package com.chuck.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText

class JokesListRobot {

    fun checkJokes() = apply {
        onView(withId(com.chuck.R.id.recycler_view)).check(matches(isDisplayed()))
        onView(withText(JOKE_1_TEXT)).check(matches(isDisplayed()))
        onView(withText(JOKE_4_TEXT)).check(matches(isDisplayed()))
    }

    companion object {
        const val JOKE_1_TEXT = "Chuck Norris joke 1"
        const val JOKE_4_TEXT = "Chuck Norris joke 4"
    }
}
