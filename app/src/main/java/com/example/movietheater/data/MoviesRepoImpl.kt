package com.example.movietheater.data

import com.example.movietheater.data.remote.MoviesApi
import com.example.movietheater.data.ui.model.UiMovieModel

class MoviesRepoImpl(private val moviesApi: MoviesApi) : MoviesRepo {

    override suspend fun getMovies(): List<UiMovieModel> =
        moviesApi.getMovies().movies.map { it.mapToUi() }

}