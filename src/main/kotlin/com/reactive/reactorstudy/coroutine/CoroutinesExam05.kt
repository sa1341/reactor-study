package com.reactive.reactorstudy.coroutine

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val time = measureTimeMillis {
        val name = async { getName() }
        val lastName = async { getLastName() }
        println("Hello, ${name.await()} ${lastName.await()}")
    }

    println("Execution took $time ms")
}

suspend fun getName(): String {
    delay(1000)
    return "Junyoung"
}

suspend fun getLastName(): String {
    delay(1000)
    return "Minwan"
}







