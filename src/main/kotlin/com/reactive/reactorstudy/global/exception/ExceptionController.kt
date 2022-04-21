package com.reactive.reactorstudy.global.exception

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.RuntimeException

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(value = [Exception::class])
    fun exception(e: Exception): String? {
        println("런타임 예외 발생!!!!")
        return e.message
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun runtimeException(e: RuntimeException): String? {
        println("런타임 예외 발생!!!!")
        return e.message
    }
}
