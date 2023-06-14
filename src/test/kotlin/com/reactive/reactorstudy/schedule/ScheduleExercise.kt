package com.reactive.reactorstudy.schedule

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit

private val log = KotlinLogging.logger {}

class ScheduleExercise {

    @Test
    fun immediateScheduleTest() {
        // given
        for (i in 1..100) {
            Flux.create<Int> {
                log.debug { "next = $i" }
                it.next(i)
            }.subscribeOn(
                Schedulers.immediate()
            ).subscribe {
                log.debug { "value = $it" }
            }
        }
    }

    @Test
    fun singleScheduleTest() {
        // given
        for (i in 1..100) {
            Flux.create<Int> {
                log.debug { "next = $i" }
                it.next(i)
            }.subscribeOn(
                Schedulers.single()
            ).subscribe {
                log.debug { "value = $it" }
            }
        }

        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    fun parallelScheduleTest() {
        // given
        for (i in 1..100) {
            Flux.create<Int> {
                log.debug { "next = $i" }
                it.next(i)
            }.subscribeOn(
                Schedulers.parallel()
            ).subscribe {
                log.debug { "value = $it" }
            }
        }

        TimeUnit.SECONDS.sleep(2)
    }

    @Test
    fun boundedElasticScheduleTest() {
        // given
        for (i in 1..200) {
            Flux.create<Int> {
                log.debug { "next = $i" }
                it.next(i)
            }.subscribeOn(
                Schedulers.boundedElastic()
            ).subscribe {
                log.debug { "value = $it" }
            }
        }

        TimeUnit.SECONDS.sleep(1)
    }
}
