package com.example.movietheater.movieslistscreen.di

import com.example.movietheater.movieslistscreen.ui.movieListAdapterDelegate
import com.example.movietheater.movieslistscreen.ui.viewmodel.MoviesListViewModel
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieListModule = module {

    viewModel<MoviesListViewModel> {
        MoviesListViewModel(get())
    }

    factory {
        ListDelegationAdapter(
            movieListAdapterDelegate()
        )
    }
}