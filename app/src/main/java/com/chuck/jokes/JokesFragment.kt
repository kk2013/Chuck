package com.chuck.jokes

import android.os.Bundle
import android.util.Log
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
    private val jokesAdapter: JokesAdapter = JokesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentJokesBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = jokesAdapter

        jokesViewModel.jokesList.observe(this, Observer {
            Log.i("JOKES", "" + it.size)
            jokesAdapter.submitList(it)
        })
        jokesViewModel.networkState?.observe(this, Observer {
            Log.i("JOKES", "" + it.status.name)
            jokesAdapter.setNetworkState(it)
        })

//        jokesViewModel.refreshJokes()

        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jokesViewModel.refreshJokes()
    }*/
}