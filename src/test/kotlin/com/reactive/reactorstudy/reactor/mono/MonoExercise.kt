package com.reactive.reactorstudy.reactor.mono

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono

private val log = KotlinLogging.logger {}

class MonoExercise {

    @Test
    fun deferTest() {
        // given
        Mono.defer {
            Mono.just(1)
        }.subscribe {
            log.debug { "next: $it" }
        }
    }
}
