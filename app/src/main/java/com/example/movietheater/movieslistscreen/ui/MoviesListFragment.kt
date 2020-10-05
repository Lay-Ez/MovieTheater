package com.example.movietheater.movieslistscreen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.base.Status
import com.example.movietheater.movieslistscreen.ui.viewmodel.MoviesListViewModel
import com.example.movietheater.movieslistscreen.ui.viewmodel.MoviesViewState
import com.example.movietheater.movieslistscreen.ui.viewmodel.UiEvent
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_movies_list.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val viewModel: MoviesListViewModel by viewModel()
    private val adapter: ListDelegationAdapter<List<ListItem>> = get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            displayState(it)
        })
        viewModel.processUiEvent(UiEvent.LoadMovies)
    }

    private fun displayState(it: MoviesViewState) {
        when (it.status) {
            Status.CONTENT -> {
                adapter.items = it.movieList
                adapter.notifyDataSetChanged()
            }
            Status.PROCESSING -> {

            }
            Status.ERROR -> {

            }
        }
    }

    private fun setupAdapter() {
        moviesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        moviesRecyclerView.adapter = adapter
    }
}