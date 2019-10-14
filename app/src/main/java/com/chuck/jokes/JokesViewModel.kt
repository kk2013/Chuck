package com.chuck.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.chuck.data.ChuckJokeRepository
import com.chuck.data.JokesDataSourceFactory
import com.chuck.data.NetworkState
import com.chuck.model.Joke
import javax.inject.Inject

class JokesViewModel @Inject constructor(jokeRepository: ChuckJokeRepository
) : ViewModel() {

    private val dataSourceFactory: JokesDataSourceFactory = jokeRepository.createDataSourceFactory()
    var jokesRepo: LiveData<PagedList<Joke>>
    var networkState: LiveData<NetworkState>?

    init {
        jokesRepo = jokeRepository.getJokes(dataSourceFactory)
        networkState = switchMap(dataSourceFactory.jokesLiveData, { it.networkState })
    }
}