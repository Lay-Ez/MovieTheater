package com.example.movietheater.base.extensions

import java.util.*

fun Date.retrieveYear(): Int {
    val calendar = Calendar.getInstance().apply { time = this@retrieveYear }
    return calendar.get(Calendar.YEAR)
}