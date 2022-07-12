package com.reactive.reactorstudy.reactor

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping(path = ["/api/v1"])
@RestController
class ReactiveApi {

    @GetMapping(path = ["/users/{name}"])
    fun getName(@PathVariable(value = "name") name: String): Mono<String> {
        return Mono.just(name)
    }
}
