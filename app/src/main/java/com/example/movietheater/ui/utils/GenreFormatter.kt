package com.example.movietheater.ui.utils

fun formatGenres(genres: List<String>): String {
    val sb = StringBuilder()
    genres.forEachIndexed { index, genreModel ->
        sb.append(genreModel)
        if (index < genres.size - 1) {
            sb.append(", ")
        }
    }
    return sb.toString()
}