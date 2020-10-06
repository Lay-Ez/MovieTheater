package com.example.movietheater.movieslistscreen.ui.viewmodel

import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.ui.model.UiMovieModel

data class MoviesViewState(
    val status: Status,
    val movieList: List<UiMovieModel>
)

sealed class DataEvent : Event {
    data class OnMoviesLoaded(val movieList: List<UiMovieModel>) : DataEvent()
    data class OnError(val error: Throwable) : DataEvent()
}

sealed class UiEvent : Event {
    object LoadMovies : UiEvent()
}