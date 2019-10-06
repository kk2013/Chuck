package com.chuck.joke

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.chuck.di.ChuckViewModelFactory
import com.chuck.databinding.FragmentJokeBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class JokeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private lateinit var jokeViewModel: JokeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    private fun onJokeStateChange(state: JokeViewModel.JokeState?) {
        when (val jokeState = state!!) {
            is JokeViewModel.JokeState.Loading -> showLoading()
            is JokeViewModel.JokeState.Empty -> showEmptyState()
            is JokeViewModel.JokeState.Success -> showJoke()
        }
    }

    private fun showJoke() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showEmptyState() {

    }

    private fun showLoading() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentJokeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jokeViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(JokeViewModel::class.java)
        jokeViewModel.loadJoke()
    }
}