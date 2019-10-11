package com.chuck.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke

class JokesDataSourceFactory(private val service: ChuckJokeService) : DataSource.Factory<Int, Joke>() {
    private val sourceLiveData = MutableLiveData<JokeDataSource>()
    override fun create(): DataSource<Int, Joke> {
        val source = JokeDataSource(service)
        sourceLiveData.postValue(source)
        return source
    }
}