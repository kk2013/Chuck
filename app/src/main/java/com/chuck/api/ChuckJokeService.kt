package com.chuck.api

import com.chuck.model.JokeResponse
import retrofit2.http.GET

interface ChuckJokeService {

    @GET("/jokes/random")
    suspend fun getRandomJoke(): JokeResponse

    @GET
    suspend fun getCustomNameJoke(): JokeResponse

    @GET
    suspend fun getJokes(): List<JokeResponse>
}
