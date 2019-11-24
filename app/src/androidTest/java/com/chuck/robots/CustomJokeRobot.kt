package com.chuck.robots

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.chuck.R

class CustomJokeRobot {

    fun checkCustomNameJokeScreen() = apply {
        onView(withId(R.id.name_label))
            .check(matches(withText("Enter a first and last name to be used in the joke")))
        onView(withId(R.id.name_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.done_button)).check(matches(isDisplayed()))
    }

    fun selectDoneButton() = apply {
        onView(withId(R.id.done_button)).perform(click())
    }

    fun checkError() = apply {
        onView(withText("Please check that you have entered a first and last name")).check(
            matches(
                isDisplayed()
            )
        )
    }

    fun closeDialog() = apply {
        onView(withText(DONE)).perform(click())
    }

    fun enterName() = apply {
        onView(withId(R.id.name_edit_text)).perform(clearText(), typeText("John Smith"))
    }

    fun closeKeyboard() = apply {
        closeSoftKeyboard()
    }

    fun checkJokeText() = apply {
        onView(withId(android.R.id.message)).check(matches(withText(JOKE_TEST)))
    }

    companion object {
        const val JOKE_TEST = "Chuck Norris joke text"
        const val DONE = "Done"
    }

    /*onView(withId(R.id.name_label))
    .check(matches(withText("Enter a first and last name to be used in the joke")))
    onView(withId(R.id.name_edit_text)).check(matches(isDisplayed()))
    onView(withId(R.id.done_button)).check(matches(isDisplayed()))

    onView(withId(R.id.done_button)).perform(click())

    onView(withText("Please check that you have entered a first and last name")).check(
    matches(
    isDisplayed()
    )
    )
    onView(withText(DONE)).perform(click())

    onView(withId(R.id.name_edit_text)).perform(clearText(), typeText("John Smith"))
    closeSoftKeyboard()
    onView(withId(R.id.done_button)).perform(click())
    onView(withId(android.R.id.message)).check(matches(withText(JOKE_TEST)))
    onView(withText(DONE)).perform(click())  */
}
