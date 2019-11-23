package com.chuck.jokeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chuck.databinding.FragmentJokesBinding
import com.chuck.di.ChuckViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class JokesListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ChuckViewModelFactory

    private val jokesViewModel: JokesListViewModel by viewModels {
        viewModelFactory
    }

    private val jokesAdapter: JokesAdapter = JokesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentJokesBinding.inflate(inflater, container, false)

        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = jokesAdapter

        jokesViewModel.jokesRepo.observe(this, Observer {
            jokesAdapter.submitList(it)
        })
        jokesViewModel.networkState?.observe(this, Observer {
            jokesAdapter.setNetworkState(it)
        })

        return binding.root
    }
}