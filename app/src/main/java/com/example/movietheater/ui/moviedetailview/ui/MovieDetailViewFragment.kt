package com.example.movietheater.ui.moviedetailview.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.extensions.getYear
import com.example.movietheater.base.extensions.round
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.MovieDetailViewModel
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.UiEvent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie_detail_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MovieDetailViewFragment : Fragment(R.layout.fragment_movie_detail_view) {

    private val args: MovieDetailViewFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState.status) {
                Status.CONTENT -> {
                    displayLoad(false)
                    viewState.movie?.let { displayMovie(it) }
                }
                Status.ERROR -> {
                    displayLoad(false)
                    viewState.error?.let { displayError(it) }
                }
                Status.PROCESSING -> {
                    displayLoad(true)
                }
            }
        })
        viewModel.processUiEvent(UiEvent.LoadMovie(args.movieId))
    }

    private fun displayMovie(movie: UiMovieModel) {
        Glide.with(constrainLayout).load(movie.posterImagePath).into(posterImageView)
        titleTextView.text = movie.title
        yearTextView.text = getYear(movie.releaseDate)
        genreTextView.text = formatGenres(movie.genres)
        scoreTextView.text = movie.voteAvg.round(1).toString()
        voteCountTextView.text = movie.voteCount.toString()
        desciptionTextView.text = movie.overview
    }

    private fun displayError(error: Throwable) {
        nestedScrollView.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        when (error) {
            is IOException -> {
                Snackbar.make(toolbar, R.string.network_error_msg, Snackbar.LENGTH_LONG).show()
            }
            else -> {
                Snackbar.make(toolbar, R.string.generic_error_msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun displayLoad(isLoading: Boolean) {
        if (isLoading) {
            nestedScrollView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            nestedScrollView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun formatGenres(genres: List<String>): String {
        val sb = StringBuilder()
        genres.forEachIndexed { index, genreModel ->
            sb.append(genreModel)
            if (index < genres.size - 1) {
                sb.append(", ")
            }
        }
        return sb.toString()
    }
}