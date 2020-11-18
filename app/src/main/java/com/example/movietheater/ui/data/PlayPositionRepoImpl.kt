package com.example.movietheater.ui.data

import android.content.SharedPreferences

class PlayPositionRepoImpl(private val sharedPreferences: SharedPreferences) : PlayPositionsRepo {

    override fun savePlayPosition(videoUri: String, playPosition: Long) {
        sharedPreferences
            .edit()
            .putLong(videoUri, playPosition)
            .apply()
    }

    override fun getPlayPosition(videoUri: String): Long =
        sharedPreferences
            .getLong(videoUri, 0)

}