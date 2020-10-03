package com.example.movietheater.data.ui.model

data class UiMovieModel(
    val adult: Boolean,
    val genres: List<String>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val releaseDate: String,
    val posterImagePath: String,
    val popularity: Double,
    val title: String,
    val videoPath: String,
    val voteAvg: Double,
    val voteCount: Int
)