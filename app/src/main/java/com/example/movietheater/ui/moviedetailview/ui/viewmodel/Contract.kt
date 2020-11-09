package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.ui.data.model.UiMovieModel

data class MovieDetailViewState(
    val status: Status,
    val movie: UiMovieModel?,
    val error: Throwable?
)

sealed class DataEvent : Event {
    data class OnMovieLoaded(val movie: UiMovieModel) : DataEvent()
    data class OnError(val error: Throwable) : DataEvent()
}

sealed class UiEvent : Event {
    data class LoadMovie(val movieId: Int) : UiEvent()
}