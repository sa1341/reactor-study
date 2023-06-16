package com.reactive.reactorstudy.reactor.flux

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.TimeUnit

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

    @Test
    fun mergeSequenceTest() {
        // given
        val flux1 = Flux.range(1, 3)
            .doOnSubscribe {
                log.debug { "doOnSubscribe1" }
            }.delayElements(Duration.ofMillis(100))

        val flux2 = Flux.range(10, 3)
            .doOnSubscribe {
                log.debug { "doOnSubscribe2" }
            }.delayElements(Duration.ofMillis(100))

        // when
        Flux.mergeSequential(flux1, flux2)
            .doOnNext {
                log.debug { "doOnNext: $it" }
            }.subscribe()

        TimeUnit.SECONDS.sleep(1)
    }

    private fun shouldDoOnError(t: Throwable): Int {
        log.error { "ERROR: $t" }
        return 1
    }
}
