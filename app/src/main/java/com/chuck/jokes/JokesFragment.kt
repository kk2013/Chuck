package com.chuck.jokes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chuck.databinding.FragmentJokesBinding
import com.chuck.di.ChuckViewModelFactory
import com.chuck.list.JokesAdapter
import com.chuck.list.JokesViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class JokesFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private val jokesViewModel: JokesViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var jokesAdapter: JokesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentJokesBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        jokesAdapter = JokesAdapter()
        binding.recyclerView.adapter = jokesAdapter

        jokesViewModel.state.observe(this, Observer {
            when (it) {
                is JokesViewModel.JokesState.Loading -> binding.progressBar.visibility =
                    View.VISIBLE
                is JokesViewModel.JokesState.Loaded -> binding.progressBar.visibility = View.GONE
//                is JokesViewModel.JokesState.Empty ->
                is JokesViewModel.JokesState.Success -> {
                    for (joke in it.jokes) {
                        Log.i("JOKE", joke.joke)
                    }
                    jokesAdapter.loadJokes(it.jokes)
                    jokesAdapter.notifyDataSetChanged()
                }
            }
        })
        jokesViewModel.loadJokes()

        return binding.root
    }
}