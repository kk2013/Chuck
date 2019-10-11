package com.chuck.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineContextProvider
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.joke.JokeViewModel.JokeState.Failed
import com.chuck.joke.JokeViewModel.JokeState.InvalidName
import com.chuck.joke.JokeViewModel.JokeState.Loaded
import com.chuck.joke.JokeViewModel.JokeState.Loading
import com.chuck.joke.JokeViewModel.JokeState.Success
import com.chuck.model.Joke
import com.chuck.model.JokeResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
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
    fun `test failure`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getCustomNameJoke(any(), any())).thenThrow(mockHttpException)
        whenever(mockJokeResponse.value).thenReturn(joke)

        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke("John Smith")

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Failed, actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }

    @Test
    fun `test invalid name`() = coroutineTestRule.runBlockingTest {

        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke("JohnSmith")

        verify(mockJokeRepository, never()).getCustomNameJoke(any(), any())
        assertEquals(1, actualValues.size)
        assertEquals(InvalidName, actualValues[0])
    }

    @Test
    fun `test timeout`() = coroutineTestRule.runBlockingTest {

//        whenever(mockJokeRepository.getCustomNameJoke(any(), any())).thenReturn(mockJokeResponse)
        
        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke("JohnSmith")

        verify(mockJokeRepository, never()).getCustomNameJoke(any(), any())
        assertEquals(1, actualValues.size)
        assertEquals(InvalidName, actualValues[0])
    }

    @Test
    fun `test success`() = coroutineTestRule.runBlockingTest {

        val joke = Joke(1, "Some Chuck joke", emptyList())

        whenever(mockJokeRepository.getCustomNameJoke(any(), any())).thenReturn(mockJokeResponse)
        whenever(mockJokeResponse.value).thenReturn(joke)

        jokeViewModel.state.observeForever(observer)

        jokeViewModel.loadJoke("John Smith")

        assertEquals(3, actualValues.size)
        assertEquals(Loading, actualValues[0])
        assertEquals(Success(joke.joke), actualValues[1])
        assertEquals(Loaded, actualValues[2])
    }

    @Test
    fun `invalid name with only one name`() {
        val names = jokeViewModel.validName("John")

        assertTrue(names.isEmpty())
    }

    @Test
    fun `valid name`() {
        val names = jokeViewModel.validName("John Smith")

        assertFalse(names.isEmpty())
        assertEquals("John", names[1])
        assertEquals("Smith", names[2])
    }
}