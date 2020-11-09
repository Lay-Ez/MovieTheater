package com.example.movietheater.ui

import com.example.movietheater.data.remote.model.GenreModel
import com.example.movietheater.data.remote.model.RemoteMovieModel

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