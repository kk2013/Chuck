package com.chuck.data

import androidx.paging.PageKeyedDataSource
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke
import kotlinx.coroutines.runBlocking

class JokeDataSource(
    private val service: ChuckJokeService
) : PageKeyedDataSource<Int, Joke>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Joke>
    ) {
        runBlocking {
            try {
                val jokes = service.getJokes(NUMBER_OF_JOKES, 1, params.requestedLoadSize)
                callback.onResult(jokes.value, null, 2)
            } catch (ex: Exception) {

            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        runBlocking {
            try {
                val page = params.key
                val jokes = service.getJokes(NUMBER_OF_JOKES, page, params.requestedLoadSize)
                callback.onResult(jokes.value, page - 1)
            } catch (ex: Exception) {

            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Joke>) {
        runBlocking {
            try {
                val page = params.key
                val jokes = service.getJokes(NUMBER_OF_JOKES, page, params.requestedLoadSize)
                callback.onResult(jokes.value, page + 1)
            } catch (ex: Exception) {

            }
        }
    }

    companion object {
        private const val NUMBER_OF_JOKES = 12
    }
}