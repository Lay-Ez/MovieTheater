package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.movietheater.base.viewmodel.BaseViewModel
import com.example.movietheater.base.viewmodel.Event
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.data.PlayPositionsRepo
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.data.model.mapToUi
import kotlinx.coroutines.launch
import java.io.IOException

class MovieDetailViewModel(
    private val moviesRepo: MoviesRepo,
    private val playPositionsRepo: PlayPositionsRepo
) :
    BaseViewModel<MovieDetailViewState>() {

    override fun initialViewState(): MovieDetailViewState =
        MovieDetailViewState(
            status = Status.PROCESSING,
            movie = null,
            error = null,
            playState = PlayState(
                videoUri = "",
                savedPlayPosition = 0
            )
        )

    override fun reduce(event: Event, previousState: MovieDetailViewState): MovieDetailViewState? {
        when (event) {
            is UiEvent.LoadMovie -> loadMovie(event.movieId)
            is UiEvent.OnSavePlayPosition -> savePlayPosition(event.playPosition)
            is DataEvent.OnMovieLoaded -> return onMovieLoaded(event.movie)
            is DataEvent.OnError -> return onError(event.error)
            is DataEvent.OnLoadStarted -> onLoad()
        }
        return null
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
        val currentPlayState = viewState.value?.playState
        currentPlayState?.let {
            playPositionsRepo.savePlayPosition(it.videoUri, playPosition)
        }
    }

    private fun onMovieLoaded(
        movie: UiMovieModel
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.CONTENT,
            movie = movie,
            error = null,
            playState = PlayState(
                videoUri = movie.videoUri,
                savedPlayPosition = playPositionsRepo.getPlayPosition(movie.videoUri)
            )
        )
    }

    private fun onError(
        error: Throwable
    ): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.ERROR,
            movie = null,
            error = error,
            playState = PlayState(
                videoUri = "",
                savedPlayPosition = 0
            )
        )
    }

    private fun onLoad(): MovieDetailViewState {
        return MovieDetailViewState(
            status = Status.PROCESSING,
            movie = null,
            error = null,
            playState = PlayState(
                videoUri = "",
                savedPlayPosition = 0
            )
        )
    }
}