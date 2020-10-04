package com.example.movietheater.data.remote.model

import com.google.gson.annotations.SerializedName

data class GenreModel(
    @SerializedName("name")
    val name: String
)