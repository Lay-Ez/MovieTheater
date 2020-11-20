package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.ui.data.model.UiMovieModel

sealed class MovieDetailViewState {

    data class Content(
        val movie: UiMovieModel,
        val playState: PlayState
    ) : MovieDetailViewState()

    data class Error(
        val error: Throwable
    ) : MovieDetailViewState()

    object Loading : MovieDetailViewState()
}

data class PlayState(
    val videoUri: String,
    val savedPlayPosition: Long
)

sealed class DataEvent : Event {
    data class OnMovieLoaded(val movie: UiMovieModel) : DataEvent()
    data class OnError(val error: Throwable) : DataEvent()
    object OnLoadStarted : DataEvent()
}

sealed class UiEvent : Event {
    data class LoadMovie(val movieId: Int) : UiEvent()
    data class OnSavePlayPosition(val playPosition: Long) : UiEvent()
}