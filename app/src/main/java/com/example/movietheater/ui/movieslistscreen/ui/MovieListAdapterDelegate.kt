package com.example.movietheater.ui.movieslistscreen.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.ListItem
import com.example.movietheater.base.extensions.retrieveYear
import com.example.movietheater.ui.data.model.UiMovieModel
import com.example.movietheater.ui.utils.formatGenres
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer
import kotlinx.android.synthetic.main.movie_list_item.view.*

fun movieListAdapterDelegate(onClick: (UiMovieModel, ImageView) -> Unit): AdapterDelegate<List<ListItem>> =
    adapterDelegateLayoutContainer<UiMovieModel, ListItem>(
        R.layout.movie_list_item
    ) {
        bind {
            itemView.titleTextView.text = item.title
            itemView.yearTextView.text = item.releaseDate.retrieveYear().toString()
            itemView.ratingTextView.text = item.voteAvg.toString()
            itemView.genreTextView.text = formatGenres(item.genres)
            itemView.posterImageView.transitionName = item.posterImagePath
            Glide.with(containerView)
                .load(item.posterImagePath)
                .into(itemView.posterImageView)
            containerView.cardView.setOnClickListener { onClick(item, itemView.posterImageView) }
        }
    }