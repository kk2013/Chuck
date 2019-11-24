package com.chuck.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chuck.TestCoroutineRule
import com.chuck.api.ChuckJokeApi
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class ChuckJokeRepositoryTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    private var mockService: ChuckJokeApi = mock()
    private var mockDataSourceFactory: JokesDataSourceFactory = mock()

    private lateinit var chuckRepo: ChuckJokeRepository

    @Before
    fun setUp() {
        chuckRepo = ChuckJokeRepository(mockService)
    }

    @Test
    fun `create data source factory`() {

        val dataSourceFactory = chuckRepo.createDataSourceFactory()

        assertNotNull(dataSourceFactory)
    }

    @Test
    fun `get jokes`() {

        val pagedList = chuckRepo.getJokes(mockDataSourceFactory)

        assertNotNull(pagedList)
    }

    @Test
    fun `correct api call is made when custom name joke is retrieved`() = coroutineTestRule.runBlockingTest {

        chuckRepo.getCustomNameJoke(anyString(), anyString())

        verify(mockService).getCustomNameJoke(anyString(), anyString())
    }

    @Test
    fun `correct api is called when random joke is retrieved`() = coroutineTestRule.runBlockingTest {

        chuckRepo.getRandomJoke()

        verify(mockService).getRandomJoke()
    }
}
