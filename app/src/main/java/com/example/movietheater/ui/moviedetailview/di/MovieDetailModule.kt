package com.example.movietheater.ui.moviedetailview.di

import com.example.movietheater.ui.moviedetailview.ui.viewmodel.MovieDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieDetailModule = module {

    viewModel<MovieDetailViewModel> {
        MovieDetailViewModel(get())
    }

    single {
        ExoPlayerProvider(androidContext())
    }
}