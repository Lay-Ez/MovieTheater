package com.example.movietheater.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteMovieModel(
    @SerializedName("adult")
    val adult: Boolean,
    @SerializedName("genres")
    val genres: List<GenreModel>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("overview")
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("poster_path")
    val posterImagePath: String,
    @SerializedName("popularity")
    val popularity: Double,
    @SerializedName("title")
    val title: String,
    @SerializedName("video")
    val videoPath: String,
    @SerializedName("vote_average")
    val voteAvg: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)