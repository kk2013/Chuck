package com.chuck.data

import com.chuck.api.ChuckJokeApi
import com.nhaarman.mockito_kotlin.mock
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class ChuckJokeRepositoryTest {

    private var mockService: ChuckJokeApi = mock()
    private var mockDataSourceFactory: JokesDataSourceFactory = mock()

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