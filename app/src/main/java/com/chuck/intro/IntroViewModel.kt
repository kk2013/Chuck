package com.chuck.intro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.util.wrapEspressoIdlingResource
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class IntroViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository
) : ViewModel() {

    internal val state = MutableLiveData<IntroState>()

    sealed class IntroState {
        object Loading : IntroState()
        object Failed : IntroState()
        data class Success(val jokeText: String) : IntroState()
    }

    fun loadJoke() = viewModelScope.launch {
        state.value = IntroState.Loading
        wrapEspressoIdlingResource {
            state.value = try {
                val jokeResponse = jokeRepository.getRandomJoke()
                // Intentionally slow down to simulate network delay
                delay(3000)
                IntroState.Success(jokeResponse.value.joke)
            } catch (ex: Exception) {
                IntroState.Failed
            }
        }
    }
}
