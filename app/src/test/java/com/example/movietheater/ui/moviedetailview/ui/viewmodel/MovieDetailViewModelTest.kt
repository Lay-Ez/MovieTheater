package com.example.movietheater.ui.moviedetailview.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.CoroutineRule
import com.example.movietheater.ui.captureViewState
import com.example.movietheater.ui.data.PlayPositionsRepo
import com.example.movietheater.ui.data.model.mapToUi
import com.example.movietheater.ui.remoteMovieModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class MovieDetailViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineRule = CoroutineRule()

    private val moviesRepo: MoviesRepo = mock()
    private val viewStateObserver: Observer<MovieDetailViewState> = mock()
    private val playPositionsRepo: PlayPositionsRepo = mock()
    private lateinit var viewModel: MovieDetailViewModel

    @Before
    fun setUp() {
        viewModel = MovieDetailViewModel(moviesRepo, playPositionsRepo)
        viewModel.viewState.observeForever(viewStateObserver)
        whenever(playPositionsRepo.getPlayPosition(any())).thenReturn(0)
    }

    @Test
    fun `test movie request successful`() {
        runBlocking {
            whenever(moviesRepo.getMovie(any())).thenReturn(remoteMovieModel)
        }
        viewModel.processUiEvent(UiEvent.LoadMovie(1))
        val viewState = captureViewState(viewStateObserver)
        assertTrue(viewState is MovieDetailViewState.Content)
        assertEquals(remoteMovieModel.mapToUi(), (viewState as MovieDetailViewState.Content).movie)
    }

    @Test
    fun `test movie request error`() {
        runBlocking {
            whenever(moviesRepo.getMovie(any())).then { throw IOException() }
        }
        viewModel.processUiEvent(UiEvent.LoadMovie(1))
        val viewState = captureViewState(viewStateObserver)
        assertTrue(viewState is MovieDetailViewState.Error)
        assertTrue((viewState as MovieDetailViewState.Error).error is IOException)
    }
}