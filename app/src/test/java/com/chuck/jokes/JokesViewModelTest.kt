package com.chuck.jokes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineContextProvider
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.jokes.JokesViewModel
import com.chuck.jokes.JokesViewModel.JokesState.Failed
import com.chuck.jokes.JokesViewModel.JokesState.Loaded
import com.chuck.jokes.JokesViewModel.JokesState.Loading
import com.chuck.jokes.JokesViewModel.JokesState.Success
import com.chuck.model.Joke
import com.chuck.model.JokesResponse
import com.nhaarman.mockito_kotlin.any
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

class JokesViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    @Mock
    lateinit var mockJokeRepository: ChuckJokeRepository
    @Mock
    lateinit var mockJokesResponse: JokesResponse
    @Mock
    lateinit var mockHttpException: HttpException

    private lateinit var observer: Observer<JokesViewModel.JokesState>

    private val actualValues = mutableListOf<JokesViewModel.JokesState>()

    private lateinit var jokesViewModel: JokesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        observer = Observer {
            actualValues.plusAssign(it)
        }
        jokesViewModel = JokesViewModel(mockJokeRepository, TestCoroutineContextProvider())
    }

    @After
    fun tearDown() {
        actualValues.clear()
    }

    @Test
    fun `testFailed`() = coroutineTestRule.runBlockingTest {

        val jokes = emptyList<Joke>()

        whenever(mockJokeRepository.getJokes(any())).thenThrow(mockHttpException)
        whenever(mockJokesResponse.value).thenReturn(jokes)

        jokesViewModel.state.observeForever(observer)

        jokesViewModel.loadJokes()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Failed, actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }

    @Test
    fun `testSuccess`() = coroutineTestRule.runBlockingTest {

        val jokes = emptyList<Joke>()

        whenever(mockJokeRepository.getJokes(any())).thenReturn(mockJokesResponse)
        whenever(mockJokesResponse.value).thenReturn(jokes)

        jokesViewModel.state.observeForever(observer)

        jokesViewModel.loadJokes()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Success(jokes), actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }
}