package com.reactive.reactorstudy.customer.api

import com.reactive.reactorstudy.customer.domain.Customer
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.kotlin.core.publisher.toMono

@Component
class CustomRouter {

    @Bean
    fun customerRoutes(): RouterFunction<*> = router {
        "/functional".nest {
            "/customer".nest {
                GET("/") {
                    ServerResponse.ok().body(Customer(1, "functional web").toMono(), Customer::class.java)
                }
            }
        }
    }
}
