package com.chuck.data

import com.chuck.api.ChuckJokeService
import javax.inject.Inject

class ChuckJokeRepository @Inject constructor(private val service: ChuckJokeService) {

    suspend fun getRandomJoke() = service.getRandomJoke()

    suspend fun getCustomNameJoke(name: String) = service.getCustomNameJoke(name)

    suspend fun getJokes(numberOfJokes: Int) = service.getJokes(numberOfJokes)
}