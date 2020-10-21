package com.example.movietheater.data.ui.model

import android.os.Parcelable
import com.example.movietheater.base.ListItem
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class UiMovieModel(
    val adult: Boolean,
    val genres: List<String>,
    val id: Int,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val releaseDate: Date,
    val posterImagePath: String,
    val popularity: Double,
    val title: String,
    val videoPath: String,
    val voteAvg: Double,
    val voteCount: Int
) : ListItem, Parcelable