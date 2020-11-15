package com.example.movietheater.ui.movieslistscreen.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movietheater.base.viewmodel.BaseViewModel
import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.data.model.mapToUi
import kotlinx.coroutines.launch
import java.io.IOException

class MoviesListViewModel(private val moviesRepo: MoviesRepo) : BaseViewModel<MoviesViewState>() {

    override fun initialViewState(): MoviesViewState = MoviesViewState(
        status = Status.PROCESSING,
        movieList = listOf(),
        error = null
    )

    override fun reduce(event: Event, previousState: MoviesViewState): MoviesViewState? {
        when (event) {
            is UiEvent.LoadMovies -> loadMovies()
            is DataEvent.OnMoviesLoaded -> return onMoviesLoaded(event.movieList)
            is DataEvent.OnError -> return onError(event.error)
            is DataEvent.OnLoadStarted -> return onLoad()
        }
        return null
    }

    private fun loadMovies() {
        processDataEvent(DataEvent.OnLoadStarted)
        viewModelScope.launch {
            try {
                val moviesList = moviesRepo.getMovies()
                processDataEvent(DataEvent.OnMoviesLoaded(
                    moviesList.map { it.mapToUi() }
                ))
            } catch (e: IOException) {
                processDataEvent(DataEvent.OnError(e))
            }
        }
    }

    private fun onMoviesLoaded(moviesList: List<UiMovieModel>): MoviesViewState {
        return MoviesViewState(Status.CONTENT, moviesList, error = null)
    }

    private fun onError(e: Throwable): MoviesViewState {
        return MoviesViewState(status = Status.ERROR, movieList = listOf(), error = e)
    }

    private fun onLoad(): MoviesViewState {
        return MoviesViewState(
            status = Status.PROCESSING,
            movieList = listOf(),
            error = null
        )
    }
}