package com.example.movietheater.ui.moviedetailview.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movietheater.R
import com.example.movietheater.base.extensions.retrieveYear
import com.example.movietheater.base.extensions.round
import com.example.movietheater.ui.data.model.UiMovieModel
import kotlinx.android.synthetic.main.fragment_movie_detail_view.*

class MovieDetailViewFragment : Fragment(R.layout.fragment_movie_detail_view) {

    private val args: MovieDetailViewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayMovie(args.movieModel)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun displayMovie(movie: UiMovieModel) {
        Glide.with(constrainLayout).load(movie.posterImagePath).into(posterImageView)
        titleTextView.text = movie.title
        yearTextView.text = movie.releaseDate.retrieveYear().toString()
        genreTextView.text = formatGenres(movie.genres)
        scoreTextView.text = movie.voteAvg.round(1).toString()
        voteCountTextView.text = movie.voteCount.toString()
        desciptionTextView.text = movie.overview
    }

    private fun formatGenres(genres: List<String>): String {
        val sb = StringBuilder()
        genres.forEachIndexed { index, genreModel ->
            sb.append(genreModel)
            if (index < genres.size - 1) {
                sb.append(", ")
            }
        }
        return sb.toString()
    }
}