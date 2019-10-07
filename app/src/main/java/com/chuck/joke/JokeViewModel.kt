package com.chuck.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JokeViewModel @Inject constructor(private val jokeRepository: ChuckJokeRepository) : ViewModel() {

    private val _state = MutableLiveData<JokeState>()
    val state: MutableLiveData<JokeState>
        get() = _state

    sealed class JokeState {
        object Loading : JokeState()
        object Loaded : JokeState()
        object Empty : JokeState()
        data class Success(val jokeText: String) : JokeState()
    }

    fun loadJoke() {
        _state.value = JokeState.Loading
        viewModelScope.launch {
            val jokeResponse = withContext(Dispatchers.IO) {
                jokeRepository.getRandomJoke()
            }
            _state.value = JokeState.Loaded
            _state.value = JokeState.Success(jokeResponse.value.joke)
        }
    }
}