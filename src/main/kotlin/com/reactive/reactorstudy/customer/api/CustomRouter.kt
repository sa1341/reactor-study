package com.reactive.reactorstudy.customer.api

import com.reactive.reactorstudy.customer.handler.CustomHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.router

@Component
class CustomRouter(
    private val customHandler: CustomHandler
) {

    @Bean
    fun customerRoutes() = router {
        "/functional".nest {
            "/customer".nest {
                GET("/", customHandler::get)
            }
        }
    }
}
