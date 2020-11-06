package com.example.movietheater.base.extensions

import junit.framework.Assert.assertEquals
import org.junit.Test

class DoubleExtensionsTest {

    @Test
    fun `test round`() {
        assertEquals(8.4, 8.415.round(1))
        assertEquals(10.5, 10.46.round(1))
        assertEquals(10.5, 10.5.round(1))
        assertEquals(0.4, 0.400001.round(1))
        assertEquals(0.0, 0.01.round(1))
    }
}