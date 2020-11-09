package com.example.movietheater.data.remote.model

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val movies: List<RemoteMovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)