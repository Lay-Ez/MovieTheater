package com.example.movietheater.ui.movieslistscreen.ui.viewmodel

import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.ui.data.model.UiMovieModel

sealed class MoviesViewState {

    data class Content(
        val movieList: List<UiMovieModel>
    ) : MoviesViewState()

    data class Error(
        val error: Throwable
    ) : MoviesViewState()

    object Loading : MoviesViewState()
}


sealed class DataEvent : Event {
    data class OnMoviesLoaded(val movieList: List<UiMovieModel>) : DataEvent()
    data class OnError(val error: Throwable) : DataEvent()
    object OnLoadStarted : DataEvent()
}

sealed class UiEvent : Event {
    object LoadMovies : UiEvent()
    object ForceLoadMovies : UiEvent()
}