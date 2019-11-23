package com.chuck.joke

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuck.data.ChuckJokeRepository
import com.chuck.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

class JokeViewModel @Inject constructor(
    private val jokeRepository: ChuckJokeRepository
) : ViewModel() {

    internal val state = MutableLiveData<JokeState>()

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
            state.value = JokeState.InvalidName
        } else {
            state.value = JokeState.Loading
            wrapEspressoIdlingResource {
                state.value = try {
                    val jokeResponse = jokeRepository.getCustomNameJoke(names[1], names[2])
                    JokeState.Success(jokeResponse.value.joke)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    JokeState.Failed
                }
                state.value = JokeState.Loaded
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