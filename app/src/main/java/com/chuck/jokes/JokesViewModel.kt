package com.chuck.jokes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.di.CoroutineContextProvider
import com.chuck.model.Joke
import com.chuck.utils.Constants.Companion.TIMEOUT
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class JokesViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository,
    private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    private val _state = MutableLiveData<JokesState>()
    val state: MutableLiveData<JokesState>
        get() = _state

    sealed class JokesState {
        object Loading : JokesState()
        object Loaded : JokesState()
        object Failed : JokesState()
        data class Success(val jokes: List<Joke>) : JokesState()
    }

    fun loadJokes() = viewModelScope.launch {
        _state.value = JokesState.Loading
        try {
            val jokesResponse = withTimeout(TIMEOUT) {
                withContext(contextProvider.IO) {
                    jokeRepository.getJokes(5)
                }
            }
            _state.value = JokesState.Success(jokesResponse.value)
        } catch (ex: Exception) {
            _state.value = JokesState.Failed
        }
        _state.value = JokesState.Loaded
    }
}