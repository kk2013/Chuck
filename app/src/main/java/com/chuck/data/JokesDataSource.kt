package com.chuck.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke
import kotlinx.coroutines.runBlocking

class JokesDataSource(
    private val service: ChuckJokeService
) : PageKeyedDataSource<Int, Joke>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Joke>
    ) {
        runBlocking {
            try {
                networkState.postValue(NetworkState.LOADING)
                val jokes = service.getJokes(NUMBER_OF_JOKES, 1, params.requestedLoadSize)
                callback.onResult(jokes.value, null, 2)
                networkState.postValue(NetworkState.SUCCESS)
            } catch (ex: Exception) {
                networkState.postValue(NetworkState.FAILED)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        //Not required as will only load after the initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        runBlocking {
            try {
                networkState.postValue(NetworkState.LOADING)
                val page = params.key
                val jokes = service.getJokes(NUMBER_OF_JOKES, page, params.requestedLoadSize)
                callback.onResult(jokes.value, page + 1)
                networkState.postValue(NetworkState.SUCCESS)
            } catch (ex: Exception) {
                networkState.postValue(NetworkState.FAILED)
            }
        }
    }

    companion object {
        private const val NUMBER_OF_JOKES = 12
        private const val LOG_TAG = "Data source"
    }
}