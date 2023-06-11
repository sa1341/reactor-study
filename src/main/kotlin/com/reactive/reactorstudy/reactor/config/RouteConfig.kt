package com.reactive.reactorstudy.reactor.config

import com.reactive.reactorstudy.reactor.filter.CustomHandlerFilter
import com.reactive.reactorstudy.reactor.handler.PlayHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration
class RouteConfig() {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun route(playerHandler: PlayHandler): RouterFunction<ServerResponse> {
        logger.info("Routing Start!!!!!!!")

        return RouterFunctions
            .route(GET("/players/{name}"), playerHandler::getName)
            .filter(CustomHandlerFilter())
    }
}
