package com.reactive.reactorstudy.reactor.filter

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

class CustomHandlerFilter : HandlerFilterFunction<ServerResponse, ServerResponse> {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun filter(request: ServerRequest, handlerFunction: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        logger.info("CustomHandlerFilter Start !!!!!!!")

        if (request.pathVariable("name").equals("test", ignoreCase = true)) {
            return ServerResponse.status(FORBIDDEN).build()
        }

        return handlerFunction.handle(request)
    }
}
