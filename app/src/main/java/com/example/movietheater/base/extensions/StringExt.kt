package com.example.movietheater.base.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toDate(format: String): Date {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    try {
        return dateFormat.parse(this)
    } catch (e: ParseException) {
        throw ParseException(
            "Cannot parse string $this to date with specified format: $format",
            e.errorOffset
        )
    }
}