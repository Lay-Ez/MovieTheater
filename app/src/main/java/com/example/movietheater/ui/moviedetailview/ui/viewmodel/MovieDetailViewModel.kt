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
            error = null
        )

    override fun reduce(event: Event, previousState: MovieDetailViewState): MovieDetailViewState? {
        when (event) {
            is UiEvent.LoadMovie -> loadMovie(event.movieId)
            is DataEvent.OnMovieLoaded -> return onMovieLoaded(event.movie)
            is DataEvent.OnError -> return onError(event.error)
        }
        return null
    }

    private fun loadMovie(movieId: Int) {
        viewState.value = MovieDetailViewState(
            status = Status.PROCESSING,
            movie = null,
            error = null
        )
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
    }

    private fun onMovieLoaded(
        movie: UiMovieModel
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.CONTENT,
            movie = movie,
            error = null
        )
    }

    private fun onError(
        error: Throwable
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.ERROR,
            movie = null,
            error = error
        )
    }
}