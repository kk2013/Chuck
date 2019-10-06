package com.chuck.joke

//import com.chucknorris.jokes.ChuckNorrisService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class JokeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableLiveData<JokeState>()
    val state: MutableLiveData<JokeState>
        get() = _state

    sealed class JokeState {
        object Loading : JokeState()
        object Empty : JokeState()
        data class Success(val jokeText: String) : JokeState()
    }

    fun loadJoke() {
        /*_state.value = JokeState.Loading
        viewModelScope.launch {
            val jokeText = withContext(Dispatchers.IO) {
                service.getRandomJoke()
            }
            _state.value = JokeState.Success(jokeText)
        }*/
    }
}