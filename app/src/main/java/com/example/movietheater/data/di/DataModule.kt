package com.example.movietheater.data.di

import com.example.movietheater.data.remote.MoviesApi
import com.example.movietheater.data.remote.MoviesRepo
import com.example.movietheater.data.remote.MoviesRepoImpl
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<MoviesApi> {
        get<Retrofit>()
            .create(MoviesApi::class.java)
    }

    single<MoviesRepo> {
        MoviesRepoImpl(get())
    }
}