package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movietheater.base.viewmodel.BaseViewModel
import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.data.PlayPositionsRepo
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.data.model.mapToUi
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDetailViewModel(
    private val moviesRepo: MoviesRepo,
    private val playPositionsRepo: PlayPositionsRepo
) : BaseViewModel<MovieDetailViewState>() {

    override fun initialViewState(): MovieDetailViewState =
        MovieDetailViewState.Loading

    override fun reduce(event: Event, previousState: MovieDetailViewState): MovieDetailViewState? {
        when (event) {
            is UiEvent.LoadMovie -> onLoadMovie(event.movieId)
            is UiEvent.OnSavePlayPosition -> savePlayPosition(event.playPosition)
            is DataEvent.OnMovieLoaded -> return onMovieLoaded(event.movie)
            is DataEvent.OnError -> return onError(event.error)
            is DataEvent.OnLoadStarted -> return onLoad()
        }
        return null
    }

    private fun onLoadMovie(movieId: Int) {
        val currentViewState = viewState.value
        if (currentViewState is MovieDetailViewState.Content) {
            val isRequestedMovieCashed = currentViewState.movie.id == movieId
            if (!isRequestedMovieCashed) {
                loadMovie(movieId)
            }
        } else {
            loadMovie(movieId)
        }
    }

    private fun loadMovie(movieId: Int) {
        processDataEvent(DataEvent.OnLoadStarted)
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

    private fun savePlayPosition(playPosition: Long) {
        val currentViewState = viewState.value
        if (currentViewState is MovieDetailViewState.Content) {
            playPositionsRepo.savePlayPosition(currentViewState.movie.videoUri, playPosition)
        }
    }

    private fun onMovieLoaded(
        movie: UiMovieModel
    ): MovieDetailViewState {
        return MovieDetailViewState.Content(
            movie = movie,
            playState = PlayState(
                videoUri = movie.videoUri,
                savedPlayPosition = playPositionsRepo.getPlayPosition(movie.videoUri)
            )
        )
    }

    private fun onError(
        error: Throwable
    ): MovieDetailViewState {
        return MovieDetailViewState.Error(
            error = error
        )
    }

    private fun onLoad(): MovieDetailViewState {
        return MovieDetailViewState.Loading
    }
}