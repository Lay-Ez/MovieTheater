package com.example.movietheater.base.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.ParseException
import java.util.*

class StringExtKtTest {

    @Test
    fun `test toDate`() {
        val dateStr = "2014-08-15"
        val format = "yyyy-MM-dd"
        val date = dateStr.toDate(format)
        val calendar = Calendar.getInstance(Locale.getDefault()).apply { time = date }
        assertEquals(2014, calendar.get(Calendar.YEAR))
        assertEquals(7, calendar.get(Calendar.MONTH)) //Months start with 0
        assertEquals(15, calendar.get(Calendar.DAY_OF_MONTH))
    }

    @Test(expected = ParseException::class)
    fun `test toDate error`() {
        val dateStr = "2014-08-15"
        val format = "yyyy:MM:dd"
        val date = dateStr.toDate(format)
    }
}