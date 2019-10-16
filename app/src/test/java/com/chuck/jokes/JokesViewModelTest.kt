package com.chuck.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.chuck.data.ChuckJokeRepository
import com.chuck.data.JokesDataSource
import com.chuck.data.JokesDataSourceFactory
import com.chuck.data.NetworkState
import com.chuck.model.Joke
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokesViewModelTest {
    
    @Mock
    private lateinit var mockJokeRepository: ChuckJokeRepository
    @Mock
    private lateinit var mockDataSourceFactory: JokesDataSourceFactory
    @Mock
    private lateinit var mockRepo: MutableLiveData<JokesDataSource>

    private lateinit var jokesViewModel: JokesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(mockJokeRepository.createDataSourceFactory()).thenReturn(mockDataSourceFactory)
        whenever(mockDataSourceFactory.jokesLiveData).thenReturn(mockRepo)
    }

    @Test
    fun `init`() {
        jokesViewModel = JokesViewModel(mockJokeRepository)

        verify(mockJokeRepository).createDataSourceFactory()
        verify(mockJokeRepository).getJokes(any())
    }
}