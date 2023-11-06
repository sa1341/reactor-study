package com.reactive.reactorstudy.coroutine.basic

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val executor = Executors.newSingleThreadScheduledExecutor {
    Thread(it, "scheduler").apply { isDaemon = true }
}

suspend fun main() {
    println("Before")

    /*suspendCoroutine<Unit> { continuation ->
        println("Before too")
        continuation.resume(Unit)
    }*/

    delay(1000)

    println("After")
}

suspend fun delay(timeMills: Long): Unit =
    suspendCoroutine { cont ->
        executor.schedule({
            cont.resume(Unit)
        }, timeMills, TimeUnit.MILLISECONDS)
    }
