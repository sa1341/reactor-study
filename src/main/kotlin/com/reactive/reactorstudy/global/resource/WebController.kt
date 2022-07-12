package com.reactive.reactorstudy.global.resource

import com.reactive.reactorstudy.global.AsyncService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class WebController(
    private val asyncService: AsyncService
) {

    var log: Logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/index")
    fun index(): String {
        return "index.html"
    }

    @GetMapping("/async-test")
    fun testAsync() {

        log.info("TEST ASYNC")

        for (i in 1..10) {
            asyncService.asyncMethod(i)
        }
    }
}
