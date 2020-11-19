package com.example.movietheater.ui.moviedetailview.di

import android.content.Context
import com.google.android.exoplayer2.Player
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
                player!!.addListener(object : Player.EventListener {
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            player!!.apply {
                                seekTo(0)
                                playWhenReady = false
                            }
                        }
                    }
                })
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