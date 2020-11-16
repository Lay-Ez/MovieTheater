package com.example.movietheater.ui.utils

import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer

fun SimpleExoPlayer.onError(onError: (error: ExoPlaybackException) -> Unit) {
    addListener(object : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException) {
            onError(error)
        }
    })
}