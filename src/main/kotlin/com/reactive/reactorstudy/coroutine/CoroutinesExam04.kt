package com.reactive.reactorstudy.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 원자성 위반 
 */
var counter = 0

fun main() = runBlocking {

    val workerA = asyncIncrement(2000)
    val workerB = asyncIncrement(100)

    workerA.await()
    workerB.await()
    println("counter [$counter]")
}

fun asyncIncrement(by: Int) = GlobalScope.async {
    for (i in 0 until by) {
        counter++
    }
}







