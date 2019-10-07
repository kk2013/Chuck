package com.chuck.joke

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chuck.databinding.FragmentJokeBinding
import com.chuck.di.ChuckViewModelFactory
import com.chuck.list.JokesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class JokeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private val jokeViewModel: JokeViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentJokeBinding.inflate(inflater, container, false)

        jokeViewModel.state.observe(this, Observer {
            when (it) {
                is JokeViewModel.JokeState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is JokeViewModel.JokeState.Loaded -> binding.progressBar.visibility = View.GONE
//                is JokeViewModel.JokeState.Empty -> showEmptyState()
                is JokeViewModel.JokeState.Success -> {
                    binding.joke = it.jokeText
                    Log.i("JOKE", it.jokeText)
                }
            }
        })
        jokeViewModel.loadJoke()

        return binding.root
    }
}