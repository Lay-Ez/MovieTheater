package com.example.movietheater.ui.movieslistscreen.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.movieslistscreen.ui.viewmodel.MoviesListViewModel
import com.example.movietheater.ui.movieslistscreen.ui.viewmodel.MoviesViewState
import com.example.movietheater.ui.movieslistscreen.ui.viewmodel.UiEvent
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_movies_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException


class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val viewModel: MoviesListViewModel by viewModel()
    private val adapter: ListDelegationAdapter<List<ListItem>> = ListDelegationAdapter(
        movieListAdapterDelegate { uiMovieModel, imageView ->
            openDetailedViewForMovie(uiMovieModel, imageView)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setupAdapter()
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            displayState(it)
        })
        viewModel.processUiEvent(UiEvent.LoadMovies)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.processUiEvent(UiEvent.ForceLoadMovies)
        }
    }

    private fun displayState(viewState: MoviesViewState) {
        when (viewState) {
            is MoviesViewState.Content -> {
                adapter.items = viewState.movieList
                adapter.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
            is MoviesViewState.Loading -> {
                swipeRefreshLayout.isRefreshing = true
            }
            is MoviesViewState.Error -> {
                swipeRefreshLayout.isRefreshing = false
                displayError(viewState.error)
            }
        }
    }

    private fun displayError(error: Throwable) {
        when (error) {
            is IOException -> {
                Snackbar.make(toolbar, R.string.network_error_msg, Snackbar.LENGTH_LONG).show()
            }
            else -> {
                Snackbar.make(toolbar, R.string.generic_error_msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupAdapter() {
        moviesRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        moviesRecyclerView.adapter = adapter
        moviesRecyclerView.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }

    private fun openDetailedViewForMovie(movie: UiMovieModel, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to movie.posterImagePath)
        val action =
            MoviesListFragmentDirections.toMovieDetailViewFragment(movie.id, movie.posterImagePath)
        findNavController().navigate(action, extras)
    }
}