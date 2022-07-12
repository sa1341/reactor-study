package com.reactive.reactorstudy.global

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class AsyncService {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    fun asyncMethod(i: Int) {
        try {
            TimeUnit.MILLISECONDS.sleep(500)
            log.info("[AsyncMethod] - $i")
        } catch (e: InterruptedException) {
            log.error("ERROR: $e")
        }
    }
}
