package com.example.movietheater.ui.moviedetailview.di

import android.content.Context
import com.google.android.exoplayer2.SimpleExoPlayer

class ExoPlayerProvider(private val context: Context) {

    @Volatile
    private var player: SimpleExoPlayer? = null

    fun getPlayer(): SimpleExoPlayer {
        if (player == null) {
            synchronized(this) {
                player = SimpleExoPlayer.Builder(context).build().apply {
                    playWhenReady = false
                    seekTo(0)
                }
            }
        }
        return player!!
    }

    fun releasePlayer() {
        synchronized(this) {
            player?.release()
            player = null
        }
    }
}