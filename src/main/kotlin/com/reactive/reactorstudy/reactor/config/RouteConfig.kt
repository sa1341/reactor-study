package com.reactive.reactorstudy.reactor.config

import com.reactive.reactorstudy.reactor.filter.CustomHandlerFilter
import com.reactive.reactorstudy.reactor.handler.PlayHandler
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

private val logger = KotlinLogging.logger {}

@Configuration
class RouteConfig() {

    @Bean
    fun route(playerHandler: PlayHandler): RouterFunction<ServerResponse> {
        logger.info { "Start Player Handler" }

        return RouterFunctions
            .route(GET("/players/{name}"), playerHandler::getName)
            .filter(CustomHandlerFilter())
    }
}
