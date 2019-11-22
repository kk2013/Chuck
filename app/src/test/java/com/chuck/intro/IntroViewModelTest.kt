package com.chuck.intro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.intro.IntroViewModel.IntroState.Failed
import com.chuck.intro.IntroViewModel.IntroState.Loaded
import com.chuck.intro.IntroViewModel.IntroState.Loading
import com.chuck.intro.IntroViewModel.IntroState.Success
import com.chuck.model.Joke
import com.chuck.model.JokeResponse
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

class IntroViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private var mockJokeRepository: ChuckJokeRepository = mock()
    private var mockJokeResponse: JokeResponse = mock()
    private var mockHttpException: HttpException = mock()

    private lateinit var observer: Observer<IntroViewModel.IntroState>

    private val actualValues = mutableListOf<IntroViewModel.IntroState>()

    private lateinit var introViewModel: IntroViewModel

    @Before
    fun setUp() {

        observer = Observer {
            actualValues.plusAssign(it)
        }
        introViewModel = IntroViewModel(mockJokeRepository)
    }

    @After
    fun tearDown() {
        actualValues.clear()
    }

    @Test
    fun `the correct states are set when an exception is thrown by the joke repo`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getRandomJoke()).thenThrow(mockHttpException)
        whenever(mockJokeResponse.value).thenReturn(joke)

        introViewModel.state.observeForever(observer)

        introViewModel.loadJoke()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Failed, actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }

    @Test
    fun `the correct states are set when a success response is returned by the joke repo`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getRandomJoke()).thenReturn(mockJokeResponse)
        whenever(mockJokeResponse.value).thenReturn(joke)

        introViewModel.state.observeForever(observer)

        introViewModel.loadJoke()

        coroutineTestRule.advanceTime(3000)

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Success(joke.joke), actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }
}