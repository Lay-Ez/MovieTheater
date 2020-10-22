package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movietheater.base.viewmodel.BaseViewModel
import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.data.model.mapToUi
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDetailViewModel(private val moviesRepo: MoviesRepo) :
    BaseViewModel<MovieDetailViewState>() {

    override fun initialViewState(): MovieDetailViewState =
        MovieDetailViewState(
            status = Status.PROCESSING,
            movie = null,
            error = null,
            playerViewState = PlayerViewState(
                isPlaying = false,
                playTimeInMs = 0
            )
        )

    override fun reduce(event: Event, previousState: MovieDetailViewState): MovieDetailViewState? {
        when (event) {
            is UiEvent.LoadMovie -> return loadMovie(event.movieId, previousState)
            is DataEvent.OnMovieLoaded -> return onMovieLoaded(event.movie, previousState)
            is DataEvent.OnError -> return onError(event.error, previousState)
        }
        return null
    }

    private fun loadMovie(movieId: Int, previousState: MovieDetailViewState): MovieDetailViewState {
        viewModelScope.launch {
            try {
                val movie = moviesRepo.getMovie(movieId)?.mapToUi()
                if (movie != null) {
                    processDataEvent(DataEvent.OnMovieLoaded(movie))
                }
            } catch (e: IOException) {
                processDataEvent(DataEvent.OnError(e))
            }
        }
        return MovieDetailViewState(
            status = Status.PROCESSING,
            movie = null,
            error = null,
            playerViewState = previousState.playerViewState
        )
    }

    private fun onMovieLoaded(
        movie: UiMovieModel,
        previousState: MovieDetailViewState
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.CONTENT,
            movie = movie,
            error = null,
            playerViewState = previousState.playerViewState
        )
    }

    private fun onError(
        error: Throwable,
        previousState: MovieDetailViewState
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.ERROR,
            movie = null,
            error = error,
            playerViewState = previousState.playerViewState
        )
    }
}