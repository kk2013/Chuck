package com.chuck.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineContextProvider
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.joke.JokeViewModel.JokeState.Failed
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
import retrofit2.HttpException

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
    @Mock
    lateinit var mockHttpException: HttpException

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
    fun `testFailure`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getRandomJoke()).thenThrow(mockHttpException)
        whenever(mockJokeResponse.value).thenReturn(joke)

        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Failed, actualValues[1])
        assertEquals(Loaded, actualValues[2])
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
        assertEquals(Success(joke.joke), actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }
}