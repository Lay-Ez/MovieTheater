package com.example.movietheater.ui.movieslistscreen.ui

import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.base.extensions.retrieveYear
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.utils.formatGenres
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.movie_list_item.view.*

fun movieListAdapterDelegate(onClick: (UiMovieModel) -> Unit): AdapterDelegate<List<ListItem>> =
    adapterDelegateLayoutContainer<UiMovieModel, ListItem>(
        R.layout.movie_list_item
    ) {
        bind {
            findViewById<TextView>(R.id.titleTextView).text = item.title
            findViewById<TextView>(R.id.yearTextView).text =
                item.releaseDate.retrieveYear().toString()
            findViewById<TextView>(R.id.ratingTextView).text = item.voteAvg.toString()
            try {
                findViewById<TextView>(R.id.genreTextView).text = formatGenres(item.genres)
            } catch (e: Exception) {
                // View exists only in portrait mode
            }
            Glide.with(containerView)
                .load(item.posterImagePath)
                .into(findViewById(R.id.posterImageView))
            containerView.cardView.setOnClickListener { onClick(item) }
        }
    }