package com.chuck.jokes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import com.chuck.databinding.FragmentJokeBinding
import com.chuck.databinding.FragmentJokesBinding
import com.chuck.di.ChuckViewModelFactory
import com.chuck.joke.JokeViewModel
import com.chuck.list.JokesViewModel
import com.chuck.utils.autoCleared
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JokesFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private lateinit var jokesViewModel: JokesViewModel

    var binding by autoCleared<FragmentJokesBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bindingData = FragmentJokesBinding.inflate(inflater, container, false)
        binding = bindingData
        return bindingData.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jokesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JokesViewModel::class.java)
        jokesViewModel.state.observe(this, Observer {
            when (it) {
                is JokesViewModel.JokesState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is JokesViewModel.JokesState.Loaded -> binding.progressBar.visibility = View.GONE
//                is JokesViewModel.JokesState.Empty ->
                is JokesViewModel.JokesState.Success -> for(joke in it.jokes) {
                    Log.i("JOKE", joke.joke)
                }
            }
        })
        jokesViewModel.loadJokes()
    }
}