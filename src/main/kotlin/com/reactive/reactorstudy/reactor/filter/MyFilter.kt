package com.reactive.reactorstudy.reactor.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class MyFilter: WebFilter {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

        logger.info("MyFilter Start!!!!!!!")

        exchange.response
            .headers.add("web-filter", "web-filter-test")

        return chain.filter(exchange)
    }
}
