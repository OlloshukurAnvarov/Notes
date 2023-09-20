package com.leaf.notes.extensions

import java.util.concurrent.Executors

fun launchThread(l: () -> Unit) {
    Executors.newSingleThreadExecutor().execute {
        l.invoke()
    }
}