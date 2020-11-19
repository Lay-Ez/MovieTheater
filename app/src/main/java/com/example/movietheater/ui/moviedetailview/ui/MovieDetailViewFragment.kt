package com.example.movietheater.ui.moviedetailview.ui

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
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
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.PlayState
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.UiEvent
import com.example.movietheater.ui.utils.formatGenres
import com.example.movietheater.ui.utils.onError
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

    private val contentViews by lazy {
        listOf(
            titleTextView, yearTextView, genreTextView, scoreTextView, voteCountTextView, ratingBar,
            divider, descriptionTextView, divider2, playerView
        )
    }

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
        startImageTransitionAnimation()
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        playerView.exo_fullscreen_icon.setOnClickListener {
            enterFullScreenMode()
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState.status) {
                Status.CONTENT -> {
                    displayLoad(false)
                    val movie = viewState.movie!!
                    displayMovie(movie)
                    processPlayState(viewState.playState)
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

    override fun onStop() {
        playerView.player = null
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.processUiEvent(UiEvent.OnSavePlayPosition(player.currentPosition))
    }

    private fun initializePlayer() {
        player.onError {
            Snackbar.make(playerView, R.string.play_error_msg, Snackbar.LENGTH_LONG).show()
        }
        playerView.player = player
    }

    private fun startImageTransitionAnimation() {
        posterImageView.transitionName = args.moviePosterUri
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        Glide.with(posterImageView)
            .load(args.moviePosterUri)
            .into(posterImageView)
    }

    private fun displayMovie(movie: UiMovieModel) {
        Glide.with(constrainLayout).load(movie.posterImagePath).into(posterImageView)
        titleTextView.text = movie.title
        yearTextView.text = movie.releaseDate.retrieveYear().toString()
        genreTextView.text = formatGenres(movie.genres)
        scoreTextView.text = movie.voteAvg.toString()
        voteCountTextView.text = movie.voteCount.toString()
        descriptionTextView.text = movie.overview
        ratingBar.rating = movie.voteAvg.toFloat()
    }

    private fun processPlayState(playState: PlayState) {
        val videoUri = playState.videoUri
        val playerNotSet = player.currentMediaItem?.playbackProperties?.uri?.toString() != videoUri
        if (playerNotSet) {
            player.apply {
                setMediaItem(MediaItem.fromUri(videoUri))
                seekTo(playState.savedPlayPosition)
                prepare()
            }
        }
    }

    private fun displayError(error: Throwable) {
        scrollView.visibility = View.INVISIBLE
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
            contentViews.forEach { it.visibility = View.GONE }
            shimmerFrameLayout.apply {
                visibility = View.VISIBLE
                startShimmer()
            }
        } else {
            shimmerFrameLayout.apply {
                stopShimmer()
                visibility = View.GONE
            }
            contentViews.forEach { it.visibility = View.VISIBLE }
        }
    }

    private fun enterFullScreenMode() {
        with(Intent(requireActivity(), FullScreenPlayerActivity::class.java)) {
            startActivity(this)
        }
    }
}