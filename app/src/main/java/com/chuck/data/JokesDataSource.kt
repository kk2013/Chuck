package com.chuck.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.chuck.api.ChuckJokeApi
import com.chuck.model.Joke
import com.chuck.util.wrapEspressoIdlingResource
import kotlinx.coroutines.runBlocking

class JokesDataSource(
    private val service: ChuckJokeApi
) : PageKeyedDataSource<Int, Joke>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Joke>
    ) {
        wrapEspressoIdlingResource {
            runBlocking {
                try {
                    networkState.postValue(NetworkState.Loading)
                    val jokes = service.getJokes(NUMBER_OF_JOKES, 1, params.requestedLoadSize)
                    callback.onResult(jokes.value, null, 2)
                    networkState.postValue(NetworkState.Success)
                } catch (ex: Exception) {
                    networkState.postValue(NetworkState.Failed)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        // Not required as will only load after the initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        wrapEspressoIdlingResource {
            runBlocking {
                try {
                    networkState.postValue(NetworkState.Loading)
                    val page = params.key
                    val jokes = service.getJokes(NUMBER_OF_JOKES, page, params.requestedLoadSize)
                    callback.onResult(jokes.value, page + 1)
                    networkState.postValue(NetworkState.Success)
                } catch (ex: Exception) {
                    networkState.postValue(NetworkState.Failed)
                }
            }
        }
    }

    companion object {
        private const val NUMBER_OF_JOKES = 12
    }
}
