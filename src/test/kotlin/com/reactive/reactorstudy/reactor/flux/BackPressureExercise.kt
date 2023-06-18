package com.reactive.reactorstudy.reactor.flux

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.time.Duration
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

class BackPressureExercise {

    @Test
    fun errorStrategyTest() {
        // given
        Flux.interval(Duration.ofMillis(1))
            .onBackpressureError()
            .doOnNext { log.info { "# doOnNext: $it" } }
            .publishOn(Schedulers.parallel())
            .subscribe {
                TimeUnit.MILLISECONDS.sleep(5)
                log.info { "# onNext: $it" }
            }
        TimeUnit.SECONDS.sleep(2)
    }
}
