package com.chuck.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executors
import javax.inject.Inject

class ChuckJokeRepository @Inject constructor(
    private val service: ChuckJokeService
) {
    suspend fun getRandomJoke() = service.getRandomJoke()

    suspend fun getCustomNameJoke(name: String) = service.getCustomNameJoke(name)

    fun getJokes(): LiveData<PagedList<Joke>> {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPrefetchDistance(5)
            .setInitialLoadSizeHint(20)
            .setPageSize(12)
            .build()

        val jokesDataSourceFactory = JokesDataSourceFactory(service)
        return  LivePagedListBuilder(jokesDataSourceFactory, pagedListConfig)
            .setInitialLoadKey(1)
            .setFetchExecutor(Executors.newFixedThreadPool(3))
            .build()
    }
}