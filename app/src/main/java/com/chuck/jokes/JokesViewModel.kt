package com.chuck.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.di.CoroutineContextProvider
import com.chuck.model.Joke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JokesViewModel @Inject constructor(private val jokeRepository: ChuckJokeRepository, private val contextProvider: CoroutineContextProvider) : ViewModel() {



    private val _state = MutableLiveData<JokesState>()
    val state: MutableLiveData<JokesState>
        get() = _state

    sealed class JokesState {
        object Loading : JokesState()
        object Loaded : JokesState()
        object Empty : JokesState()
        data class Success(val jokes: List<Joke>) : JokesState()
    }

    fun loadJokes() {
        _state.value = JokesState.Loading
        viewModelScope.launch {
            val jokesResponse = withContext(contextProvider.IO) {
                jokeRepository.getJokes(5)
            }
            _state.value = JokesState.Loaded
            _state.value = JokesState.Success(jokesResponse.value)
        }
    }
}