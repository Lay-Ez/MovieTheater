package com.example.movietheater.ui.movieslistscreen.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.ui.CoroutineRule
import com.example.movietheater.ui.captureViewState
import com.example.movietheater.ui.data.model.mapToUi
import com.example.movietheater.ui.remoteMovieModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
class MoviesListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val coroutineRule = CoroutineRule()

    private val moviesRepo: MoviesRepo = mock()
    private val viewStateObserver: Observer<MoviesViewState> = mock()
    private lateinit var viewModel: MoviesListViewModel

    @Before
    fun setUp() {
        viewModel = MoviesListViewModel(moviesRepo)
        viewModel.viewState.observeForever(viewStateObserver)
    }

    @Test
    fun `test successful movie list request`() {
        val movieList = listOf(
            remoteMovieModel
        )
        runBlocking {
            whenever(moviesRepo.getMovies()).thenReturn(movieList)
        }
        viewModel.processUiEvent(UiEvent.LoadMovies)
        val viewState = captureViewState(viewStateObserver)
        assertEquals(Status.CONTENT, viewState.status)
        assertEquals(remoteMovieModel.mapToUi(), viewState.movieList[0])
    }

    @Test
    fun `test movie list request error`() {
        runBlocking {
            whenever(moviesRepo.getMovies()).then { throw IOException() }
        }
        viewModel.processUiEvent(UiEvent.LoadMovies)
        val viewState = captureViewState(viewStateObserver)
        assertEquals(Status.ERROR, viewState.status)
    }
}