package com.example.movietheater.ui.moviedetailview.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.movietheater.R
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.MovieDetailViewModel
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.PlayerViewState
import com.example.movietheater.ui.moviedetailview.ui.viewmodel.UiEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_player.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FullScreenPlayerFragment : Fragment(R.layout.fragment_full_screen_player) {

    private val viewModel: MovieDetailViewModel by sharedViewModel()
    private lateinit var player: SimpleExoPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterFullScreen()
        initializePlayer()
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            val playerViewState = viewState.playerViewState
            displayPlayer(playerViewState)
        })
    }

    override fun onStop() {
        super.onStop()
        exitFullscreen()
        releasePlayer()
    }

    private fun enterFullScreen() {
        requireActivity().run {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                    )
            actionBar?.hide()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    private fun exitFullscreen() {
        requireActivity().run {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            actionBar?.show()
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        playerView.player = player
        playerView.exo_fullscreen_icon.setOnClickListener {
            requireActivity().onBackPressed()
        }
        playerView.setControllerVisibilityListener {
            if (it == View.GONE) {
                enterFullScreen()
            }
        }
    }

    private fun releasePlayer() {
        val playbackPosition = player.currentPosition
        val playWhenReady = player.playWhenReady
        viewModel.processUiEvent(UiEvent.SavePlayerState(playbackPosition, playWhenReady))
        player.release()
    }

    private fun displayPlayer(playerViewState: PlayerViewState) {
        player.setMediaItem(MediaItem.fromUri(playerViewState.videoUri))
        player.playWhenReady = playerViewState.playWhenReady
        player.seekTo(playerViewState.playTimeInMs)
        player.prepare()
    }
}