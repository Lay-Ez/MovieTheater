package com.example.movietheater.movieslistscreen.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movietheater.base.BaseViewModel
import com.example.movietheater.base.Event
import com.example.movietheater.base.Status
import com.example.movietheater.data.MoviesRepo
import com.example.movietheater.data.ui.model.UiMovieModel
import kotlinx.coroutines.launch
import java.io.IOException

class MoviesListViewModel(private val moviesRepo: MoviesRepo) : BaseViewModel<MoviesViewState>() {

    override fun initialViewState(): MoviesViewState = MoviesViewState(
        status = Status.PROCESSING,
        movieList = listOf()
    )

    override fun reduce(event: Event, previousState: MoviesViewState): MoviesViewState? {
        when (event) {
            is UiEvent.LoadMovies -> return loadMovies()
            is DataEvent.OnMoviesLoaded -> return onMoviesLoaded(event.movieList)
            is DataEvent.OnError -> return onError(event.error)
        }
        return null
    }

    private fun loadMovies(): MoviesViewState {
        viewModelScope.launch {
            try {
                val moviesList = moviesRepo.getMovies()
                processDataEvent(DataEvent.OnMoviesLoaded(moviesList))
            } catch (e: IOException) {
                processDataEvent(DataEvent.OnError(e))
            }
        }
        return MoviesViewState(status = Status.PROCESSING, movieList = listOf())
    }

    private fun onMoviesLoaded(moviesList: List<UiMovieModel>): MoviesViewState {
        return MoviesViewState(Status.CONTENT, moviesList)
    }

    private fun onError(e: Throwable): MoviesViewState {
        return MoviesViewState(status = Status.ERROR, movieList = listOf())
    }
}