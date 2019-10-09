package com.chuck.intro

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.chuck.R
import com.chuck.databinding.FragmentIntroBinding
import com.chuck.di.ChuckViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class IntroFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private val introViewModel: IntroViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIntroBinding.inflate(inflater, container, false)
        binding.randomJokeButton.setOnClickListener { introViewModel.loadJoke() }
        binding.customNameButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_main_fragment_to_joke_fragment)
        }
        binding.jokeListButton.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_main_fragment_to_jokes_fragment)
        }

        introViewModel.state.observe(this, Observer {
            when (it) {
                is IntroViewModel.IntroState.Loading -> binding.progressBar.visibility =
                    View.VISIBLE
                is IntroViewModel.IntroState.Loaded -> binding.progressBar.visibility = View.GONE
                is IntroViewModel.IntroState.Failed -> showDialog(getString(R.string.no_data))
                is IntroViewModel.IntroState.Success -> {
                    showDialog(it.jokeText)
                }
            }
        })

        return binding.root
    }

    private fun showDialog(jokeText: String) {
        AlertDialog.Builder(activity)
            .setCancelable(true)
            .setMessage(jokeText)
            .setNegativeButton(getString(R.string.done)) { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}