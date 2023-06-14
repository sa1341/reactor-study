package com.reactive.reactorstudy.reactor.flux

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers

private val log = KotlinLogging.logger {}

class FluxExercise {

    @Test
    fun doOnNextTest() {
        // given
        Flux.create<Int> {
            for (i in 0..4) {
                log.info { "next: $i" }
                it.next(i)
            }
        }.publishOn(
            Schedulers.single()
        ).doOnNext {
            log.debug { "donOnNext: $it" }
        }.subscribe {
            log.debug { "value: $it" }
        }
    }
}
