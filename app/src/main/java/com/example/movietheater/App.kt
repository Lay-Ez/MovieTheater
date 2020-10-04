package com.example.movietheater

import android.app.Application
import com.example.movietheater.data.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule)
        }
    }
}