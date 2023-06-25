package com.reactive.reactorstudy.reactor.testing

import org.junit.jupiter.api.Test
import org.springframework.util.Base64Utils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import reactor.test.StepVerifierOptions
import reactor.test.publisher.PublisherProbe
import reactor.test.publisher.TestPublisher
import reactor.test.scheduler.VirtualTimeScheduler
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.time.Duration

class ReactorTest {

    @Test
    fun divideTwoTest() {
        // given
        val source = Flux.just(2, 4, 6, 8, 10)
        StepVerifier
            .create(divideByTwo(source))
            .expectSubscription()
            .expectNext(1, 2, 3, 4)
            .expectError()
            .verify()
    }

    @Test
    fun takeNumberTest() {
        // given
        val source = Flux.range(0, 1000)
        StepVerifier
            .create(
                takeNumber(source, 500),
                StepVerifierOptions.create().scenarioName("Verify from 0 to 499")
            )
            .expectSubscription()
            .expectNext(0)
            .expectNextCount(498)
            .expectNext(499)
            .expectComplete()
            .verify()
    }

    @Test
    fun getCOVID19CountTest() {
        // given
        StepVerifier
            .withVirtualTime {
                getCOVID19Count(Flux.interval(Duration.ofHours(1)).take(1))
            }
            .expectSubscription()
            .then {
                VirtualTimeScheduler
                    .get()
                    .advanceTimeBy(Duration.ofHours(1))
            }
            .expectNextCount(11)
            .expectComplete()
            .verify()
    }

    @Test
    fun contextTest() {
        // given
        val source = Mono.just("hello")

        StepVerifier
            .create(
                getSecretMessage(source)
                    .contextWrite { it.put("secretMessage", "Hello, Reactor") }
                    .contextWrite { it.put("secretKey", "aGVsbG8") }
            )
            .expectSubscription()
            .expectAccessibleContext()
            .hasKey("secretKey")
            .hasKey("secretMessage")
            .then()
            .expectNext("Hello, Reactor")
            .expectComplete()
            .verify()
    }

    @Test
    fun recordWithTest() {
        // given
        StepVerifier
            .create(
                getCapitalizedCountry(
                    Flux.just("korea", "england", "canada", "india")
                )
            )
            .expectSubscription()
            .recordWith {
                arrayListOf()
            }
            .thenConsumeWhile {
                it.isNotEmpty()
            }
            .expectRecordedMatches {
                it.stream()
                    .allMatch {
                        Character.isUpperCase(it[0])
                    }
            }
            .expectComplete()
            .verify()
    }

    @Test
    fun testPublisherTest() {
        // given
        val source = TestPublisher.create<Int>()

        // when
        StepVerifier
            .create(divideByTwo(source.flux()))
            .expectSubscription()
            .then {
                source.emit(2, 4, 6, 8, 10)
            }
            .expectNext(1, 2, 3, 4)
            .expectError()
            .verify()
    }

    @Test
    fun publisherProbeTest() {
        // given
        val probe = PublisherProbe.of(supplyStandbyPower())

        // when
        StepVerifier
            .create(
                processTask(supplyMainPower(), probe.mono())
            )
            .expectNextCount(1)
            .verifyComplete()
    }

    companion object {
        fun divideByTwo(source: Flux<Int>) =
            source.zipWith(
                Flux.just(2, 2, 2, 2, 0)
            ) { x, y -> x / y }

        fun takeNumber(source: Flux<Int>, n: Long) = source.take(n)

        fun getCOVID19Count(source: Flux<Long>): Flux<Tuple2<String, Int>> {
            return source
                .flatMap {
                    Flux.just(
                        Tuples.of("서울", 10),
                        Tuples.of("경기도", 5),
                        Tuples.of("강원도", 3),
                        Tuples.of("충청도", 6),
                        Tuples.of("경상도", 5),
                        Tuples.of("전라도", 8),
                        Tuples.of("인천", 2),
                        Tuples.of("대전", 1),
                        Tuples.of("대구", 2),
                        Tuples.of("부산", 3),
                        Tuples.of("제주도", 0)
                    )
                }
        }

        fun getSecretMessage(keySource: Mono<String>): Mono<String> {
            return keySource
                .zipWith(
                    Mono.deferContextual {
                        Mono.just(it.get("secretKey") as String)
                    }
                )
                .filter {
                    it.t1 == String(Base64Utils.decodeFromString(it.t2))
                }
                .transformDeferredContextual { mono, ctx ->
                    mono.map { ctx.get("secretMessage") }
                }
        }

        fun getCapitalizedCountry(source: Flux<String>): Flux<String> {
            return source.map {
                "${it.substring(0, 1).uppercase()}${it.substring(1)}"
            }
        }

        fun processTask(main: Mono<String>, standby: Mono<String>): Mono<String> {
            return main.flatMap {
                Mono.just(it)
            }.switchIfEmpty(standby)
        }

        fun supplyMainPower() = Mono.empty<String>()

        fun supplyStandbyPower() = Mono.just("# supply Standby Power")
    }
}
