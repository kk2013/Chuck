package com.chuck.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.chuck.TestCoroutineRule
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke
import com.chuck.model.JokesResponse
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
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

@ExperimentalCoroutinesApi
class JokesDataSourceTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = TestCoroutineRule()

    /*@Mock
    private lateinit var mockInitialParams: PageKeyedDataSource.LoadInitialParams<Int>
    @Mock
    private lateinit var mockInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Joke>*/
    @Mock
    private lateinit var mockInitialCallback: PageKeyedDataSource.LoadInitialCallback<Int, Joke>
    @Mock
    private lateinit var mockCallback: PageKeyedDataSource.LoadCallback<Int, Joke>
    @Mock
    private lateinit var mockService: ChuckJokeService
    @Mock
    lateinit var mockHttpException: HttpException

    private val mockInitialParams: PageKeyedDataSource.LoadInitialParams<Int> =
        PageKeyedDataSource.LoadInitialParams(2, true)
    private val mockParams: PageKeyedDataSource.LoadParams<Int> =
        PageKeyedDataSource.LoadParams(2, 12)

    private lateinit var observer: Observer<NetworkState>

    private val actualValues = mutableListOf<NetworkState>()

    private lateinit var jokesDataSource: JokesDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        observer = Observer {
            actualValues.plusAssign(it)
        }

        jokesDataSource = JokesDataSource(mockService)
    }

    @After
    fun tearDown() {
        actualValues.clear()
    }

    @Test
    fun `initial paging load failed`() = coroutineTestRule.runBlockingTest {

        whenever(mockService.getJokes(any(), any(), any())).thenThrow(mockHttpException)

        jokesDataSource.networkState.observeForever(observer)

        jokesDataSource.loadInitial(mockInitialParams, mockInitialCallback)

        verify(mockInitialCallback, never()).onResult(emptyList(), null, 2)
        assertEquals(2, actualValues.size)
        assertEquals(Status.LOADING, actualValues[0].status)
        assertEquals(Status.FAILED, actualValues[1].status)
    }

    @Test
    fun `initial paging load successful`() = coroutineTestRule.runBlockingTest {

        whenever(mockService.getJokes(any(), any(), any())).thenReturn(
            JokesResponse(
                "",
                emptyList()
            )
        )

        jokesDataSource.networkState.observeForever(observer)

        jokesDataSource.loadInitial(mockInitialParams, mockInitialCallback)

        verify(mockInitialCallback).onResult(emptyList(), null, 2)
        assertEquals(2, actualValues.size)
        assertEquals(Status.LOADING, actualValues[0].status)
        assertEquals(Status.SUCCESS, actualValues[1].status)
    }

    @Test
    fun `load after paging load failed`() = coroutineTestRule.runBlockingTest {

        whenever(mockService.getJokes(any(), any(), any())).thenThrow(mockHttpException)

        jokesDataSource.networkState.observeForever(observer)

        jokesDataSource.loadAfter(mockParams, mockCallback)

        verify(mockCallback, never()).onResult(emptyList(), 3)
        assertEquals(2, actualValues.size)
        assertEquals(Status.LOADING, actualValues[0].status)
        assertEquals(Status.FAILED, actualValues[1].status)
    }

    @Test
    fun `load after paging load successful`() = coroutineTestRule.runBlockingTest {

        whenever(mockService.getJokes(any(), any(), any())).thenReturn(
            JokesResponse(
                "",
                emptyList()
            )
        )

        jokesDataSource.networkState.observeForever(observer)

        jokesDataSource.loadAfter(mockParams, mockCallback)

        verify(mockCallback).onResult(emptyList(), 3)
        assertEquals(2, actualValues.size)
        assertEquals(Status.LOADING, actualValues[0].status)
        assertEquals(Status.SUCCESS, actualValues[1].status)
    }
}