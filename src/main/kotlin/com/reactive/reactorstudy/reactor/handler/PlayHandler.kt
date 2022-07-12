package com.reactive.reactorstudy.reactor.handler

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

@Component
class PlayHandler {

    fun getName(request: ServerRequest): Mono<ServerResponse> {
        val name = Mono.just(request.pathVariable("name"))
        return ok().body(name, String::class.java)
    }
}
