package com.example.movietheater.base.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateExtTest {

    @Test
    fun `test retrieveYear`() {
        val expectedYear = 2014
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, expectedYear)
        }
        val date = calendar.time
        assertEquals(expectedYear, date.retrieveYear())
    }
}