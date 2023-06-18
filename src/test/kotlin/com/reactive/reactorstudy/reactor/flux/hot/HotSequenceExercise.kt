package com.reactive.reactorstudy.reactor.flux.hot

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

class HotSequenceExercise {

    @Test
    fun hotSequenceTest() {
        // given
        val singers = arrayOf(
            "Singer A",
            "Singer B",
            "Singer C",
            "Singer D",
            "Singer E"
        )

        log.info { "Begin concert" }

        val concertFlux = Flux.fromArray(singers)
            .delayElements(Duration.ofSeconds(1))
            .share()

        // when
        concertFlux.subscribe {
            log.info { "# Subscriber1 is watching ${it}\'s song" }
        }

        TimeUnit.MILLISECONDS.sleep(2500)

        concertFlux.subscribe {
            log.info { "# Subscriber2 is watching ${it}s song" }
        }

        TimeUnit.MILLISECONDS.sleep(3000)
    }
}
