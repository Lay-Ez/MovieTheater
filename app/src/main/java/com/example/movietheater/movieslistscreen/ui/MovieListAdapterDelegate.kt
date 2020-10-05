package com.example.movietheater.movieslistscreen.ui

import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.base.formatDate
import com.example.movietheater.base.round
import com.example.movietheater.data.ui.model.UiMovieModel
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

fun movieListAdapterDelegate(): AdapterDelegate<List<ListItem>> =
    adapterDelegateLayoutContainer<UiMovieModel, ListItem>(
        R.layout.movie_list_item
    ) {
        bind {
            findViewById<TextView>(R.id.titleTextView).text = item.title
            findViewById<TextView>(R.id.yearTextView).text = formatDate(item.releaseDate)
            findViewById<TextView>(R.id.ratingTextView).text = item.voteAvg.round(1).toString()
            Glide.with(containerView)
                .load(item.posterImagePath)
                .into(findViewById(R.id.posterImageView))
        }
    }