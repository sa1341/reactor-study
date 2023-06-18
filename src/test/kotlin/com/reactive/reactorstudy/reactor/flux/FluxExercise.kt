package com.reactive.reactorstudy.reactor.flux

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
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

    @Test
    fun fluxFilterTest() {
        // given
        Flux.range(1, 5)
            .filter { it % 2 == 0 }
            .doOnNext { log.debug { "doOnNext: $it" } }
            .subscribe()
    }

    @Test
    fun takeLastTest() {
        // given
        Flux.range(1, 10)
            .takeLast(5)
            .doOnNext { log.debug { "takeLast: $it" } }
            .subscribe()
    }

    @Test
    fun fluxSkipTest() {
        // given
        Flux.range(1, 10)
            .skip(5)
            .doOnNext { log.debug { "takeLast: $it" } }
            .subscribe()
    }

    @Test
    fun fluxSkipLastTest() {
        // given
        Flux.range(1, 10)
            .skipLast(5)
            .doOnNext { log.debug { "takeLast: $it" } }
            .subscribe()
    }

    @Test
    fun collectListTest() {
        // given
        Flux.range(1, 10)
            .collectList()
            .doOnNext { log.debug { "doOnNext: $it" } }
            .subscribe()
    }

    @Test
    fun cacheTest() {
        // given
        val flux = Flux.create<Int> {
            for (i in 0..3) {
                log.debug { "next: $i" }
                it.next(i)
            }
            log.debug { "complete in publisher" }
            it.complete()
        }.cache()

        flux.subscribe(
            {
                log.debug { "value: $it" }
            },
            null,
            {
                log.debug { "complete" }
            }
        )

        flux.subscribe(
            {
                log.debug { "value: $it" }
            },
            null,
            {
                log.debug { "complete" }
            }
        )
    }

    @Test
    fun flatMapTest() {
        // given
        Flux.range(1, 5)
            .flatMap { value: Int ->
                Flux.range(1, 2)
                    .map { value2: Int -> "$value , $value2" }
                    .publishOn(Schedulers.parallel())
            }
            .doOnNext { log.debug { "doOnNext: $it" } }
            .subscribe()

        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    fun deferContextualTest() {
        // given
        Flux.just(1)
            .flatMap {
                val value = it
                Mono.deferContextual {
                    val name = it.get<String>("name")
                    log.debug { "name: $name" }
                    Mono.just(value)
                }
            }.contextWrite {
                it.put("name", "junyoung")
            }.subscribe()
    }

    @Test
    fun publishOnTest() {
        // given
        Flux.range(1, 10)
            .doOnNext { log.info { "doOnNext: $it" } }
            .publishOn(Schedulers.newSingle("pub"))
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe {
                log.info("value: $it ")
            }

        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    fun subscribeOnTest() {
        // given
        Flux.range(1, 10)
            .doOnNext { log.info { "doOnNext: $it" } }
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe {
                log.info("value: $it ")
            }

        TimeUnit.SECONDS.sleep(1)
    }

    @Test
    fun fluxSinkTest() {
        // given
        val tasks = 6
        Flux.create<String> {
            for (i in 1..tasks) {
                it.next(doTask(i))
            }
        }
            .subscribeOn(Schedulers.boundedElastic())
            .doOnNext { log.debug { "# create(): $it" } }
            .publishOn(Schedulers.parallel())
            .map { "$it + success!" }
            .doOnNext { log.debug { "# map(): $it" } }
            .publishOn(Schedulers.parallel())
            .subscribe {
                log.debug { "# onNext: $it" }
            }

        TimeUnit.MILLISECONDS.sleep(500)
    }

    fun doTask(taskNumber: Int) = "task $taskNumber result"

    @Test
    fun unicastSinkTest() {
        // given
        val tasks = 6

        val unicastSink: Sinks.Many<String> = Sinks.many().unicast()
            .onBackpressureBuffer()

        val fluxView = unicastSink.asFlux()

        for (i in 1..tasks) {
            try {
                Thread {
                    unicastSink.emitNext(
                        doTask(i),
                        Sinks.EmitFailureHandler.FAIL_FAST
                    )
                    log.info { "# emitted: $i" }
                }.start()
                TimeUnit.MILLISECONDS.sleep(100)
            } catch (ie: InterruptedException) {
                log.error { "${ie.message}" }
            }
        }

        // when
        fluxView
            .publishOn(Schedulers.parallel())
            .map { "$it + success!" }
            .doOnNext { log.info { "# map(): $it" } }
            .publishOn(Schedulers.parallel())
            .subscribe {
                log.info { "# onNext: $it" }
            }

        TimeUnit.MILLISECONDS.sleep(200)
    }

    @Test
    fun backPressureTest() {
        // given
        Flux.range(1, 5)
            .doOnRequest { log.debug { "# doOnRequest: $it" } }
            .subscribe(object : BaseSubscriber<Int>() {
                override fun hookOnSubscribe(subscription: Subscription) {
                    request(1)
                }

                override fun hookOnNext(value: Int) {
                    TimeUnit.SECONDS.sleep(1)
                    log.info { "# hookOnNext: $value" }
                    request(1)
                }
            })
    }

    @Test
    fun parallelTest() {
        // given
        Flux.fromArray(arrayOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19))
            .parallel()
            .runOn(Schedulers.parallel())
            .subscribe {
                log.info { "# onNext: $it" }
            }
        TimeUnit.MILLISECONDS.sleep(100)
    }

    private fun shouldDoOnError(t: Throwable): Int {
        log.error { "ERROR: $t" }
        return 1
    }
}
