package com.example.movietheater.movieslistscreen.ui

import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.base.extensions.round
import com.example.movietheater.data.ui.model.UiMovieModel
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import java.util.*

fun movieListAdapterDelegate(): AdapterDelegate<List<ListItem>> =
    adapterDelegateLayoutContainer<UiMovieModel, ListItem>(
        R.layout.movie_list_item
    ) {
        bind {
            findViewById<TextView>(R.id.titleTextView).text = item.title
            findViewById<TextView>(R.id.yearTextView).text = getYear(item.releaseDate)
            findViewById<TextView>(R.id.ratingTextView).text = item.voteAvg.round(1).toString()
            Glide.with(containerView)
                .load(item.posterImagePath)
                .into(findViewById(R.id.posterImageView))
        }
    }

private fun getYear(date: Date): String {
    val calendar = Calendar.getInstance().apply { time = date }
    return calendar.get(Calendar.YEAR).toString()
}