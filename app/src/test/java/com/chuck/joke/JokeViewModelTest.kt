package com.chuck.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineContextProvider
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.joke.JokeViewModel.JokeState.Loaded
import com.chuck.joke.JokeViewModel.JokeState.Loading
import com.chuck.joke.JokeViewModel.JokeState.Success
import com.chuck.model.Joke
import com.chuck.model.JokeResponse
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Mock
    lateinit var mockJokeRepository: ChuckJokeRepository
    @Mock
    lateinit var mockJokeResponse: JokeResponse

    private lateinit var observer: Observer<JokeViewModel.JokeState>

    private val actualValues = mutableListOf<JokeViewModel.JokeState>()

    private lateinit var jokeViewModel: JokeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        observer = Observer {
            actualValues.plusAssign(it)
        }
        jokeViewModel = JokeViewModel(mockJokeRepository, TestCoroutineContextProvider())
    }

    @After
    fun tearDown() {
        actualValues.clear()
    }

    @Test
    fun `testSuccess`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getRandomJoke()).thenReturn(mockJokeResponse)
        whenever(mockJokeResponse.value).thenReturn(joke)

        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Loaded, actualValues[1])
        assertEquals(Success(joke.joke), actualValues[2])
    }
}