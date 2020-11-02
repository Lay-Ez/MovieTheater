package com.example.movietheater.ui.moviedetailview.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.movietheater.R
import com.example.movietheater.ui.moviedetailview.di.ExoPlayerProvider
import com.google.android.exoplayer2.SimpleExoPlayer
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import kotlinx.android.synthetic.main.fragment_full_screen_player.*
import org.koin.android.ext.android.get

class FullScreenPlayerFragment : Fragment(R.layout.fragment_full_screen_player) {

    private val playerProvider: ExoPlayerProvider = get()
    private val player: SimpleExoPlayer = playerProvider.getPlayer()
    private val onFullScreenClickListener = View.OnClickListener {
        requireActivity().onBackPressed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterFullScreen()
        initializePlayer()
    }

    override fun onStop() {
        exitFullscreen()
        super.onStop()
    }

    private fun enterFullScreen() {
        requireActivity().run {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
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
        playerView.player = player
        playerView.exo_fullscreen_icon.setOnClickListener(onFullScreenClickListener)

    }
}