package com.chuck.intro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.util.wrapEspressoIdlingResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class IntroViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository
) : ViewModel() {

    private val _state = MutableLiveData<IntroState>()
    val state: MutableLiveData<IntroState>
        get() = _state

    sealed class IntroState {
        object Loading : IntroState()
        object Loaded : IntroState()
        object Failed : IntroState()
        data class Success(val jokeText: String) : IntroState()
    }

    fun loadJoke() = viewModelScope.launch {
        _state.value = IntroState.Loading
        wrapEspressoIdlingResource {
            try {
                val jokeResponse = jokeRepository.getRandomJoke()
                delay(3000)
                _state.value =
                    IntroState.Success(jokeResponse.value.joke)
            } catch (ex: Exception) {
                _state.value = IntroState.Failed
            }
        }
        _state.value = IntroState.Loaded
    }
}