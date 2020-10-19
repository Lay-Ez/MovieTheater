package com.example.movietheater.base.extensions

fun Double.round(decimals: Int): Double = "%.${decimals}f".format(this).toDouble()