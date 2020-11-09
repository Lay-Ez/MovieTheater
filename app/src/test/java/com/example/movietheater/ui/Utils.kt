package com.example.movietheater.ui

import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify

inline fun <reified T : Any> capture(invokeCaptor: (KArgumentCaptor<T>) -> Unit): T {
    val captor = argumentCaptor<T>()
    invokeCaptor(captor)
    return captor.lastValue
}

inline fun <reified T : Any> captureViewState(observer: Observer<T>): T = capture {
    verify(observer, atLeastOnce()).onChanged(it.capture())
}