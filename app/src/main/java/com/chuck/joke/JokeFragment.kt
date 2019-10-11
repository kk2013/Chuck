package com.chuck.joke

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.chuck.R
import com.chuck.databinding.FragmentJokeBinding
import com.chuck.di.ChuckViewModelFactory
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

        binding.doneButton.setOnClickListener {
            jokeViewModel.loadJoke(binding.nameEditText.text.toString())
        }

        jokeViewModel.state.observe(this, Observer {
            when (it) {
                is JokeViewModel.JokeState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is JokeViewModel.JokeState.Loaded -> binding.progressBar.visibility = View.GONE
                is JokeViewModel.JokeState.Failed -> showDialog(getString(R.string.no_data))
                is JokeViewModel.JokeState.InvalidName -> showDialog(getString(R.string.invalid_name))
                is JokeViewModel.JokeState.Success -> {
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