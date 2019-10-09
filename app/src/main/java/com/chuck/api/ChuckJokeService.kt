package com.chuck.api

import com.chuck.model.JokeResponse
import com.chuck.model.JokesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChuckJokeService {

    @GET("/jokes/random")
    suspend fun getRandomJoke(): JokeResponse

    @GET("/jokes/random")
    suspend fun getCustomNameJoke(@Query("firstName") firstName: String): JokeResponse

    @GET("/jokes/random/{numberOfJokes}")
    suspend fun getJokes(@Path("numberOfJokes") numberOfJokes: Int): JokesResponse
}
