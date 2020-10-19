package com.example.movietheater.data

import com.example.movietheater.base.extensions.toDate
import com.example.movietheater.data.remote.model.RemoteMovieModel
import com.example.movietheater.data.ui.model.UiMovieModel

private const val DATE_FORMAT = "yyyy-MM-dd"

fun RemoteMovieModel.mapToUi() =
    UiMovieModel(
        this.adult,
        this.genres.map { it.name },
        this.id,
        this.originalLanguage,
        this.originalTitle,
        this.overview,
        this.releaseDate.toDate(DATE_FORMAT),
        this.posterImagePath,
        this.popularity,
        this.title,
        this.videoPath,
        this.voteAvg,
        this.voteCount
    )