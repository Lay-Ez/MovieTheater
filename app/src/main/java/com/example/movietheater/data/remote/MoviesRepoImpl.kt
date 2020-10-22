package com.example.movietheater.data.remote

import com.example.movietheater.data.remote.model.RemoteMovieModel

class MoviesRepoImpl(private val moviesApi: MoviesApi) :
    MoviesRepo {

    override suspend fun getMovies(): List<RemoteMovieModel> =
        moviesApi.getMovies().movies

    override suspend fun getMovie(movieId: Int): RemoteMovieModel? =
        moviesApi.getMovies().movies.find { it.id == movieId }
}