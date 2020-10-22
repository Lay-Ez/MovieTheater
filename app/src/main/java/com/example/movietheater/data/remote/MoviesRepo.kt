package com.example.movietheater.data.remote

import com.example.movietheater.data.remote.model.RemoteMovieModel

interface MoviesRepo {
    suspend fun getMovies(): List<RemoteMovieModel>
    suspend fun getMovie(movieId: Int): RemoteMovieModel?
}