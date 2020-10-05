package com.example.movietheater.base

fun formatDate(date: String): String =
    date.substringBefore("-")

fun Double.round(decimals: Int): Double = "%.${decimals}f".format(this).toDouble()
