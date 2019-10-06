package com.chuck.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.chuck.R
import com.chuck.di.ChuckViewModelFactory
import com.chuck.databinding.FragmentIntroBinding
import com.chuck.joke.JokeViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class IntroFragment : DaggerFragment() {

    /*@Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private lateinit var jokeViewModel: JokeViewModel*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIntroBinding.inflate(inflater, container, false)
        binding.randomJokeButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_main_fragment_to_joke_fragment)
        }
        binding.customNameButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_main_fragment_to_joke_fragment)
        }
        binding.jokeListButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_main_fragment_to_jokes_fragment)
        }

        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        jokeViewModel = ViewModelProviders.of(activity!!, viewModelFactory)
            .get(JokeViewModel::class.java)
        jokeViewModel.loadJoke()
    }*/
}