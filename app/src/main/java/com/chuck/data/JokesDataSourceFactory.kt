package com.chuck.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.chuck.api.ChuckJokeService
import com.chuck.model.Joke

class JokesDataSourceFactory(private val service: ChuckJokeService) : DataSource.Factory<Int, Joke>() {
    val jokesLiveData = MutableLiveData<JokesDataSource>()
    override fun create(): DataSource<Int, Joke> {
        val source = JokesDataSource(service)
        jokesLiveData.postValue(source)
        return source
    }
}