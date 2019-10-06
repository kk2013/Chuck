package com.chuck.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class JokesViewModel @Inject constructor() : ViewModel() {

    sealed class JokesState {
        object Loading : JokesState()
        object Empty : JokesState()
        data class Success(val jokeText: String) : JokesState()
    }

    val state = MutableLiveData<JokesState>().apply {
        this.value = JokesState.Loading
    }
}