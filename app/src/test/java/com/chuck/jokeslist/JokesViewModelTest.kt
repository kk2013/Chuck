package com.chuck.jokeslist

import androidx.lifecycle.MutableLiveData
import com.chuck.data.ChuckJokeRepository
import com.chuck.data.JokesDataSource
import com.chuck.data.JokesDataSourceFactory
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

    private lateinit var jokesViewModel: JokesListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(mockJokeRepository.createDataSourceFactory()).thenReturn(mockDataSourceFactory)
        whenever(mockDataSourceFactory.jokesLiveData).thenReturn(mockRepo)
    }

    @Test
    fun `init`() {
        jokesViewModel = JokesListViewModel(mockJokeRepository)

        verify(mockJokeRepository).createDataSourceFactory()
        verify(mockJokeRepository).getJokes(any())
    }
}