package com.example.movietheater.movieslistscreen.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movietheater.R
import com.example.movietheater.base.Status
import com.example.movietheater.movieslistscreen.ui.viewmodel.MoviesListViewModel
import com.example.movietheater.movieslistscreen.ui.viewmodel.MoviesViewState
import com.example.movietheater.movieslistscreen.ui.viewmodel.UiEvent
import org.koin.androidx.viewmodel.ext.android.viewModel


class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val viewModel: MoviesListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            displayState(it)
        })
        viewModel.processUiEvent(UiEvent.LoadMovies)
    }

    private fun displayState(it: MoviesViewState) {
        when (it.status) {
            Status.CONTENT -> {

            }
            Status.PROCESSING -> {

            }
            Status.ERROR -> {

            }
        }
    }
}