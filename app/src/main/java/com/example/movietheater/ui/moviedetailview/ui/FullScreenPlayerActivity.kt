package com.example.movietheater.ui.moviedetailview.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.movietheater.R
import com.example.movietheater.ui.moviedetailview.di.ExoPlayerProvider
import com.example.movietheater.ui.utils.onError
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_full_screen_player.*
import kotlinx.android.synthetic.main.exo_player_control_view.view.*
import org.koin.android.ext.android.get

class FullScreenPlayerActivity : AppCompatActivity() {

    companion object {
        private const val SYSTEM_UI_HIDE_DELAY_MS = 4000L
    }

    private val playerProvider: ExoPlayerProvider = get()
    private val player: SimpleExoPlayer = playerProvider.getPlayer()
    private val systemHideRunnable = Runnable {
        enterFullScreen()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_player)
        enterFullScreen()
        initializePlayer()
        playerView.setControllerVisibilityListener {
            if (it == View.GONE) {
                enterFullScreen()
            }
        }
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Handler(mainLooper).removeCallbacks(systemHideRunnable)
                Handler(mainLooper).postDelayed(systemHideRunnable, SYSTEM_UI_HIDE_DELAY_MS)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        exitFullscreen()
    }

    private fun enterFullScreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        actionBar?.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun exitFullscreen() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        actionBar?.show()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }

    private fun initializePlayer() {
        playerView.player = player
        player.onError {
            Snackbar.make(playerView, R.string.play_error_msg, Snackbar.LENGTH_LONG).show()
        }
        playerView.exo_fullscreen_icon.setOnClickListener {
            onBackPressed()
        }
    }
}