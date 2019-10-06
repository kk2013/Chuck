package com.chuck.model

data class Joke(
    val id: Int,
    val joke: String,
    val categories: List<String>)