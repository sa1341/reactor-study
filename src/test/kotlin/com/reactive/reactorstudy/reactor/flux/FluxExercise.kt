package com.reactive.reactorstudy.reactor.flux

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

private val log = KotlinLogging.logger {}

class FluxExercise {

    @Test
    fun concatTest() {
        // given
        val flux = Mono.justOrEmpty("jean")
            .concatWith(Mono.justOrEmpty("calm"))

        // when
        flux.subscribe(log::info)
    }

    @Test
    fun concatToMono() {
        // given
        Flux.concat(
            Flux.just("Mercury", "Venus", "Earth"),
            Flux.just("Mars", "Jupiter", "Saturn"),
            Flux.just("Uranus", "Neptune", "Pluto")
        ).collectList().subscribe {
            log.info { "$it" }
        }
    }

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

    @Test
    fun onErrorResumeTest() {
        // given
        Flux.just(1)
            .onErrorResume {
                Mono.just<Int>(shouldDoOnError(it))
            }.subscribe {
                log.debug { "value: $it" }
            }
    }

    private fun shouldDoOnError(t: Throwable): Int {
        log.error { "ERROR: $t" }
        return 1
    }
}
