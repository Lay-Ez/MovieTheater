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
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.PlayerViewState
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.UiEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie_detail_view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MovieDetailViewFragment : Fragment(R.layout.fragment_movie_detail_view) {

    private val args: MovieDetailViewFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModel()
    private lateinit var player: SimpleExoPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        initializePlayer()
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState.status) {
                Status.CONTENT -> {
                    displayLoad(false)
                    val movie = viewState.movie!!
                    val playerViewState = viewState.playerViewState
                    displayMovie(movie)
                    displayPlayer(playerViewState)
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

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
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

    private fun displayPlayer(playerViewState: PlayerViewState) {
        player.setMediaItem(MediaItem.fromUri(playerViewState.videoUri))
        player.playWhenReady = playerViewState.playWhenReady
        player.seekTo(playerViewState.playTimeInMs)
        player.prepare()
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

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        playerView.player = player
    }

    private fun releasePlayer() {
        val playbackPosition = player.currentPosition
        val playWhenReady = player.playWhenReady
        viewModel.processUiEvent(UiEvent.SavePlayerState(playbackPosition, playWhenReady))
        player.release()
    }
}