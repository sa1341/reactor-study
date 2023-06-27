package com.reactive.reactorstudy.reactor.operator

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

class OperatorExercise01 {

    @Test
    fun justOrEmptyTest() {
        // given
        Mono
            .justOrEmpty<String>(null)
            .subscribe(
                {},
                {},
                { log.info { "# onComplete" } }
            )
    }

    @Test
    fun deferTest() {
        // given
        Mono.just("Hello")
            .delayElement(Duration.ofSeconds(3))
            .switchIfEmpty(Mono.defer { sayDefault() })
            .subscribe {
                log.info { "# onNext: $it" }
            }

        TimeUnit.MILLISECONDS.sleep(3500)
    }

    @Test
    fun usingTest() {
        // given
        val path = Paths.get("/Users/limjun-young/workspace/privacy/dev/test.txt")

        // when
        Flux.using(
            {
                Files.lines(path)
            },
            {
                Flux.fromStream(it)
            },
            {
                it.close()
            }
        ).subscribe(log::info)
    }

    @Test
    fun skipTest() {
        // given
        Flux.interval(Duration.ofSeconds(1))
            .skip(2)
            .subscribe {
                log.info { "# onNext: $it" }
            }

        TimeUnit.MILLISECONDS.sleep(5500)
    }

    @Test
    fun skipDurationTest() {
        // given
        Flux.interval(Duration.ofMillis(300))
            .skip(Duration.ofSeconds(1))
            .subscribe {
                log.info { "# onNext: $it" }
            }

        TimeUnit.MILLISECONDS.sleep(2000)
    }

    companion object {
        fun sayDefault(): Mono<String> {
            log.info { "# Say Hi" }
            return Mono.just("Hi")
        }
    }
}
