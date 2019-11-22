package com.chuck.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.chuck.api.ChuckJokeApi
import com.chuck.model.Joke
import java.util.concurrent.Executors
import javax.inject.Inject

class ChuckJokeRepository @Inject constructor(
    private val service: ChuckJokeApi
) {
    suspend fun getRandomJoke() = service.getRandomJoke()

    suspend fun getCustomNameJoke(firstName: String, lastName: String) =
        service.getCustomNameJoke(firstName, lastName)

    fun getJokes(dataSourceFactory: JokesDataSourceFactory): LiveData<PagedList<Joke>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(PREFETCH_SIZE)
            .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
            .setPageSize(PAGE_SIZE)
            .build()

        return LivePagedListBuilder(dataSourceFactory, pagedListConfig)
            .setInitialLoadKey(1)
            .setFetchExecutor(Executors.newFixedThreadPool(3))
            .build()
    }

    fun createDataSourceFactory(): JokesDataSourceFactory {
        return JokesDataSourceFactory(service)
    }

    companion object {
        const val PAGE_SIZE = 12
        const val PREFETCH_SIZE = 5
        const val INITIAL_LOAD_SIZE = 20
    }
}