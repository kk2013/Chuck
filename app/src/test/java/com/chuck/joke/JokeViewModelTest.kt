package com.chuck.joke

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.chuck.TestCoroutineRule
import com.chuck.data.ChuckJokeRepository
import com.chuck.joke.JokeViewModel.JokeState.Failed
import com.chuck.joke.JokeViewModel.JokeState.Loaded
import com.chuck.joke.JokeViewModel.JokeState.Loading
import com.chuck.joke.JokeViewModel.JokeState.Success
import com.chuck.model.Joke
import com.chuck.model.JokeResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class JokeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private var mockJokeRepository: ChuckJokeRepository = mock()
    private var mockJokeResponse: JokeResponse = mock()
    private var mockHttpException: HttpException = mock()

    private lateinit var observer: Observer<JokeViewModel.JokeState>

    private val actualValues = mutableListOf<JokeViewModel.JokeState>()

    private lateinit var jokeViewModel: JokeViewModel

    @Before
    fun setUp() {

        observer = Observer {
            actualValues.plusAssign(it)
        }
        jokeViewModel = JokeViewModel(mockJokeRepository)
    }

    @After
    fun tearDown() {
        actualValues.clear()
    }

    @Test
    fun `the correct states are set when an exception is thrown by the joke repo`() =
        coroutineTestRule.runBlockingTest {

            val joke = Joke(1, "Some Chuck joke", emptyList())

            whenever(
                mockJokeRepository.getCustomNameJoke(
                    any(),
                    any()
                )
            ).thenThrow(mockHttpException)
            whenever(mockJokeResponse.value).thenReturn(joke)

            jokeViewModel.state.observeForever(observer)

            jokeViewModel.loadJoke("John Smith")

            assertEquals(3, actualValues.size)
            assertEquals(Loading, actualValues[0])
            assertEquals(Failed, actualValues[1])
            assertEquals(Loaded, actualValues[2])
        }

    @Test
    fun `the correct states are set when a success response is returned by the joke repo`() =
        coroutineTestRule.runBlockingTest {

            val joke = Joke(1, "Some Chuck joke", emptyList())

            whenever(
                mockJokeRepository.getCustomNameJoke(
                    any(),
                    any()
                )
            ).thenReturn(mockJokeResponse)
            whenever(mockJokeResponse.value).thenReturn(joke)

            jokeViewModel.state.observeForever(observer)

            jokeViewModel.loadJoke("John Smith")

            assertEquals(3, actualValues.size)
            assertEquals(Loading, actualValues[0])
            assertEquals(Success(joke.joke), actualValues[1])
            assertEquals(Loaded, actualValues[2])
        }

    @Test
    fun `empty group is returned for an input with only a first name`() {
        val names = jokeViewModel.validName("JohnSmith")

        assertTrue(names.isEmpty())
    }

    @Test
    fun `the correct groups are returned for an input with a valid name`() {
        val names = jokeViewModel.validName("John Smith")

        assertFalse(names.isEmpty())
        assertEquals("John", names[1])
        assertEquals("Smith", names[2])
    }
}