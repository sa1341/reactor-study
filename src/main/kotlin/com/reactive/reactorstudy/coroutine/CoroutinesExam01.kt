package com.reactive.reactorstudy.coroutine

import kotlinx.coroutines.*

fun main() = runBlocking {

    val deferred = async {
        delay(100)
        println("async start")
        "async result"
    }

    println("Test")

    val job = GlobalScope.launch {
        try {
            delay(200)
            println("launch Start")
            delay(Long.MAX_VALUE)
        } finally {
            println("launch Cancelled")
        }
    }

    delay(500)
    job.cancelAndJoin()

    delay(500)
    println(deferred.await())
}




