package com.chuck.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineContextProvider
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.list.JokesViewModel
import com.chuck.model.JokesResponse
import com.nhaarman.mockito_kotlin.whenever
import com.chuck.list.JokesViewModel.JokesState.Loaded
import com.chuck.list.JokesViewModel.JokesState.Loading
import com.chuck.list.JokesViewModel.JokesState.Success
import com.chuck.model.Joke
import com.nhaarman.mockito_kotlin.any
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
    fun `testSuccess`() = coroutineTestRule.runBlockingTest {

        val jokes = emptyList<Joke>()

        whenever(mockJokeRepository.getJokes(any())).thenReturn(mockJokesResponse)
        whenever(mockJokesResponse.value).thenReturn(jokes)

        jokesViewModel.state.observeForever(observer)

        jokesViewModel.loadJokes()

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Loaded, actualValues[1])
        assertEquals(Success(jokes), actualValues[2])
    }
}