package com.chuck.jokeslist

import androidx.lifecycle.MutableLiveData
import com.chuck.data.ChuckJokeRepository
import com.chuck.data.JokesDataSource
import com.chuck.data.JokesDataSourceFactory
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test

class JokesListViewModelTest {
    
    private var mockJokeRepository: ChuckJokeRepository = mock()
    private var mockDataSourceFactory: JokesDataSourceFactory = mock()
    private var mockRepo: MutableLiveData<JokesDataSource> = mock()

    private lateinit var jokesViewModel: JokesListViewModel

    @Before
    fun setUp() {

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