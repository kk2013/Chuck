package com.chuck.api

import com.chuck.model.JokeResponse
import com.chuck.model.JokesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChuckJokeApi {

    @GET("/jokes/random?escape=javascript")
    suspend fun getRandomJoke(): JokeResponse

    @GET("/jokes/random?escape=javascript")
    suspend fun getCustomNameJoke(@Query("firstName") firstName: String, @Query("lastName") lastName: String): JokeResponse

    @GET("/jokes/random/{numberOfJokes}?escape=javascript")
    suspend fun getJokes(@Path("numberOfJokes") numberOfJokes: Int,
                         @Query("page") page: Int,
                         @Query("per_page") perPage: Int): JokesResponse
}
