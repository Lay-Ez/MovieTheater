package com.example.movietheater.ui.data.di

import android.content.Context
import com.example.movietheater.ui.data.PlayPositionRepoImpl
import com.example.movietheater.ui.data.PlayPositionsRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PLAY_POSITION_PREFERENCES_FILENAME_PREFIX = "play_positions"

val uiDataModule = module {

    single<PlayPositionsRepo> {
        val playPositionPreferences = with(androidContext()) {
            getSharedPreferences(
                "$packageName.$PLAY_POSITION_PREFERENCES_FILENAME_PREFIX",
                Context.MODE_PRIVATE
            )
        }
        PlayPositionRepoImpl(playPositionPreferences)
    }

}