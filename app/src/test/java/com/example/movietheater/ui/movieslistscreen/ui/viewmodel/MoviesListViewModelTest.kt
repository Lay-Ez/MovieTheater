package com.example.movietheater.ui.movieslistscreen.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.movietheater.base.viewmodel.Status
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.data.remote.model.GenreModel
import com.example.movietheater.data.remote.model.RemoteMovieModel
import com.example.movietheater.ui.CoroutineRule
import com.example.movietheater.ui.data.model.mapToUi
import com.nhaarman.mockitokotlin2.*
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
        val remoteMovieModel = RemoteMovieModel(
            adult = false,
            genres = listOf(
                GenreModel("Drama"),
                GenreModel("Thriller")
            ),
            id = 1,
            originalLanguage = "en",
            originalTitle = "Whiplash",
            overview = "Under the direction of a ruthless instructor, a talented young drummer begins to pursue perfection at any cost, even his humanity.",
            releaseDate = "2014-10-10",
            posterImagePath = "https://upload.wikimedia.org/wikipedia/en/0/01/Whiplash_poster.jpg",
            popularity = 8.441533,
            title = "Whiplash",
            videoPath = "http://techslides.com/demos/sample-videos/small.mp4",
            voteAvg = 8.5,
            voteCount = 80
        )
        val movieList = listOf(
            remoteMovieModel
        )
        runBlocking {
            whenever(moviesRepo.getMovies()).thenReturn(movieList)
        }
        viewModel.processUiEvent(UiEvent.LoadMovies)
        val viewState = captureViewState()
        assertEquals(Status.CONTENT, viewState.status)
        assertEquals(remoteMovieModel.mapToUi(), viewState.movieList[0])
    }

    @Test
    fun `test movie list request error`() {
        runBlocking {
            whenever(moviesRepo.getMovies()).then { throw IOException() }
        }
        viewModel.processUiEvent(UiEvent.LoadMovies)
        val viewState = captureViewState()
        assertEquals(Status.ERROR, viewState.status)
    }

    private fun captureViewState(): MoviesViewState = capture {
        verify(viewStateObserver, atLeastOnce()).onChanged(it.capture())
    }
}

inline fun <reified T : Any> capture(invokeCaptor: (KArgumentCaptor<T>) -> Unit): T {
    val captor = argumentCaptor<T>()
    invokeCaptor(captor)
    return captor.lastValue
}