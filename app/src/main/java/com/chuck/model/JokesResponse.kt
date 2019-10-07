package com.chuck.model

data class JokesResponse(
    val type: String,
    val value: List<Joke>
)