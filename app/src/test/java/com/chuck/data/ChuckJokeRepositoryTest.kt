package com.chuck.data

import com.chuck.api.ChuckJokeApi
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ChuckJokeRepositoryTest {

    @Mock
    private lateinit var mockService: ChuckJokeApi
    @Mock
    private lateinit var mockDataSourceFactory: JokesDataSourceFactory

    private lateinit var chuckRepo: ChuckJokeRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

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
}