package com.example.movietheater.ui.moviedetailview.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.extensions.retrieveYear
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.moviedetailview.di.ExoPlayerProvider
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.MovieDetailViewModel
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.UiEvent
import com.example.movietheater.ui.utils.formatGenres
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import kotlinx.android.synthetic.main.fragment_movie_detail_view.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MovieDetailViewFragment : Fragment(R.layout.fragment_movie_detail_view) {

    private val args: MovieDetailViewFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModel()
    private val playerProvider: ExoPlayerProvider = get()
    private val player: SimpleExoPlayer = playerProvider.getPlayer()
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            playerProvider.releasePlayer()
            findNavController().navigateUp()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerFrameLayout = shimmerLayout as ShimmerFrameLayout
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        playerView.exo_fullscreen_icon.setOnClickListener {
            enterFullScreenMode()
        }
    }

    override fun onStart() {
        super.onStart()
        playerView.player = player
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState.status) {
                Status.CONTENT -> {
                    displayLoad(false)
                    val movie = viewState.movie!!
                    displayMovie(movie)
                    displayPlayer(movie.videoUri)
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
        if (!isContentAvailable()) {
            viewModel.processUiEvent(UiEvent.LoadMovie(args.movieId))
        }
    }

    override fun onStop() {
        playerView.player = null
        super.onStop()
    }

    private fun displayMovie(movie: UiMovieModel) {
        Glide.with(constrainLayout).load(movie.posterImagePath).into(posterImageView)
        titleTextView.text = movie.title
        yearTextView.text = movie.releaseDate.retrieveYear().toString()
        genreTextView.text = formatGenres(movie.genres)
        scoreTextView.text = movie.voteAvg.toString()
        voteCountTextView.text = movie.voteCount.toString()
        descriptionTextView.text = movie.overview
    }

    private fun displayPlayer(videoUri: String) {
        if (player.currentMediaItem?.playbackProperties?.uri.toString() != videoUri) {
            player.setMediaItem(MediaItem.fromUri(videoUri))
            player.prepare()
        }
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
            shimmerFrameLayout.visibility = View.VISIBLE
            shimmerFrameLayout.startShimmer()
            nestedScrollView.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            nestedScrollView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun enterFullScreenMode() {
        with(Intent(requireActivity(), FullScreenPlayerActivity::class.java)) {
            startActivity(this)
        }
    }

    private fun isContentAvailable(): Boolean {
        return viewModel.viewState.value?.movie?.id == args.movieId
    }
}