package com.chuck.jokes

import com.chuck.data.ChuckJokeRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class JokesViewModelTest {
    
    @Mock
    lateinit var mockJokeRepository: ChuckJokeRepository

    private lateinit var jokesViewModel: JokesViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        jokesViewModel = JokesViewModel(mockJokeRepository)
    }

    @Test
    fun `get jokes`() {

        jokesViewModel.getJokes()

        verify(mockJokeRepository).getJokes(dataSource)
    }
}