package com.reactive.reactorstudy.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    
    println("${Thread.activeCount()} threads active at the start")

    val time = measureTimeMillis {
        createCoroutines(10_000)
    }

    println("${Thread.activeCount()} threads active at the end")
    println("Took $time ms")
}

suspend fun createCoroutines(amount: Int) {

    val jobs = ArrayList<Job>()

    for (i in 1..amount) {
        jobs += GlobalScope.launch {
            println("Started $i in ${Thread.currentThread().name}")
            delay(1000)
            println("Finished $i in ${Thread.currentThread().name}")
        }
    }

    jobs.forEach {
        it.join()
    }
}




