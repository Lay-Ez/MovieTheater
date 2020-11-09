package com.example.movietheater.ui

import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor

inline fun <reified T : Any> capture(invokeCaptor: (KArgumentCaptor<T>) -> Unit): T {
    val captor = argumentCaptor<T>()
    invokeCaptor(captor)
    return captor.lastValue
}