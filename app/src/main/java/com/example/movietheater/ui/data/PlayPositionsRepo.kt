package com.example.movietheater.ui.data

interface PlayPositionsRepo {

    fun savePlayPosition(videoUri: String, playPosition: Long)

    fun getPlayPosition(videoUri: String): Long
}