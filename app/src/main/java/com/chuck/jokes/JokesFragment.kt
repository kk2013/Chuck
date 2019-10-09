package com.chuck.jokes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chuck.databinding.FragmentJokesBinding
import com.chuck.di.ChuckViewModelFactory
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
//                is JokesViewModel.JokesState.Failed ->
                is JokesViewModel.JokesState.Success -> {
                    jokesAdapter.loadJokes(it.jokes)
                    jokesAdapter.notifyDataSetChanged()
                }
            }
        })
        jokesViewModel.loadJokes()

        return binding.root
    }
}