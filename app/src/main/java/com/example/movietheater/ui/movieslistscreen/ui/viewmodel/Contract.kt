package com.example.movietheater.ui.movieslistscreen.ui.viewmodel

import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.ui.data.model.UiMovieModel

data class MoviesViewState(
    val status: Status,
    val movieList: List<UiMovieModel>,
    val error: Throwable?
)

sealed class DataEvent : Event {
    data class OnMoviesLoaded(val movieList: List<UiMovieModel>) : DataEvent()
    data class OnError(val error: Throwable) : DataEvent()
}

sealed class UiEvent : Event {
    object LoadMovies : UiEvent()
}