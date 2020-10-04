package com.example.movietheater.data

import com.example.movietheater.data.ui.model.UiMovieModel

interface MoviesRepo {
    suspend fun getMovies(): List<UiMovieModel>
}