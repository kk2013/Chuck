package com.chuck.jokes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.chuck.data.ChuckJokeRepository
import com.chuck.model.Joke
import javax.inject.Inject


class JokesViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository
) : ViewModel() {

    fun getJokes(): LiveData<PagedList<Joke>> = jokeRepository.getJokes()

    private val _state = MutableLiveData<JokesState>()
    val state: MutableLiveData<JokesState>
        get() = _state


    sealed class JokesState {
        object Loading : JokesState()
        object Loaded : JokesState()
        object Failed : JokesState()
        data class Success(val jokes: PagedList<Joke>) : JokesState()
    }
}