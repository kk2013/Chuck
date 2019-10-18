package com.chuck.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.di.CoroutineContextProvider
import com.chuck.util.wrapEspressoIdlingResource
import com.chuck.utils.Constants.Companion.TIMEOUT
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class JokeViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository,
    private val contextProvider: CoroutineContextProvider
) : ViewModel() {

    private val _state = MutableLiveData<JokeState>()
    val state: MutableLiveData<JokeState>
        get() = _state

    sealed class JokeState {
        object Loading : JokeState()
        object Loaded : JokeState()
        object Failed : JokeState()
        object InvalidName : JokeState()

        data class Success(val jokeText: String) : JokeState()
    }

    fun loadJoke(name: String) = viewModelScope.launch {
        val names = validName(name)
        if (names.isEmpty()) {
            _state.value = JokeState.InvalidName
        } else {
            _state.value = JokeState.Loading
            wrapEspressoIdlingResource {
                try {
                    val jokeResponse = withTimeout(TIMEOUT) {
                        withContext(contextProvider.IO) {
                            jokeRepository.getCustomNameJoke(names[1], names[2])
                        }
                    }
                    _state.value = JokeState.Success(jokeResponse.value.joke)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _state.value = JokeState.Failed
                }
                _state.value = JokeState.Loaded
            }
        }
    }

    fun validName(name: String): List<String> {
        // Very simple name splitting done here with just first and last name. There really should be separate
        // fields for first and last names as there are too many variations in names e.g. multiple first names,
        // double surnames etc, to be able to accurately identify which should be first and last.
        val nameRegex = "^([A-Z][a-z]{1,19})[\\s,]([A-Z][a-z]{1,19})\$".toRegex()
        val matchResult = nameRegex.find(name)
        return matchResult?.groupValues ?: emptyList()
    }
}