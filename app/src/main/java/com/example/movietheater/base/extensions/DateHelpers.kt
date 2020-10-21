package com.example.movietheater.base.extensions

import java.util.*

fun getYear(date: Date): String {
    val calendar = Calendar.getInstance().apply { time = date }
    return calendar.get(Calendar.YEAR).toString()
}