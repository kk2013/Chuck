package com.chuck.joke

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.Visibility
import com.chuck.di.ChuckViewModelFactory
import com.chuck.databinding.FragmentJokeBinding
import com.chuck.model.Joke
import com.chuck.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class JokeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private lateinit var jokeViewModel: JokeViewModel

    var binding by autoCleared<FragmentJokeBinding>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun showEmptyState() {
        //TODO Error handling
    }

    private fun showLoading() {
        //TODO Implement loading
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = FragmentJokeBinding.inflate(inflater, container, false)
        binding = dataBinding
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jokeViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JokeViewModel::class.java)
        jokeViewModel.state.observe(this, Observer {
            when (it) {
                is JokeViewModel.JokeState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is JokeViewModel.JokeState.Loaded -> binding.progressBar.visibility = View.GONE
                is JokeViewModel.JokeState.Empty -> showEmptyState()
                is JokeViewModel.JokeState.Success -> {
                    binding.joke = it.jokeText
                    Log.i("JOKE", it.jokeText)
                }
            }
        })
        jokeViewModel.loadJoke()
    }
}