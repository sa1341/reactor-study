package com.reactive.reactorstudy.reactor.operator

import com.jayway.jsonpath.JsonPath
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.reactivestreams.Subscription
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.util.Collections
import java.util.IllegalFormatException
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

    @Test
    fun `구구단 테스트`() {
        // given
        Flux.range(2, 8)
            .flatMap {
                val dan = it
                Flux.range(1, 9)
                    .map {
                        "$dan * $it = ${dan * it}"
                    }
            }.subscribe {
                log.info { "# onNext: $it" }
            }
    }

    @Test
    fun concatTest() {
        // given
        Flux
            .concat(Flux.just(1, 2, 3), Flux.just(4, 5))
            .subscribe {
                log.info { "# onNext: $it" }
            }
    }

    @Test
    fun andTest() {
        // given
        Mono.just("take1")
            .delayElement(Duration.ofSeconds(1))
            .doOnNext { log.info { "# doOnNext: $it" } }
            .and {
                Flux.just("take2", "take3")
                    .delayElements(Duration.ofMillis(600))
                    .doOnNext { log.info { "# doOnNext: $it" } }
            }.subscribe(
                {
                    log.info { "# onNext: $it" }
                },
                {
                    log.error { "# onError: $it" }
                },
                {
                    log.info { "# onComplete" }
                }
            )
    }

    @Test
    fun errorReturnTest() {
        // given
        Flux.fromIterable(listOf("blue pen", "red pen", null))
            .map { it!!.uppercase() }
            .onErrorReturn(NullPointerException::class.java, "no pen name")
            .onErrorReturn(IllegalFormatException::class.java, "illegal pen name")
            .subscribe {
                log.info { "# onNext: $it" }
            }
    }

    @Test
    fun errorResumeTest() {
        // given
        val keyword = "java"
        getBooksFromCache(keyword)
            .onErrorResume {
                getBooksFromDatabase(keyword)
            }.subscribe(
                {
                    log.info { "# onNext: $it" }
                },
                {
                    log.error { "# onError: $it" }
                }
            )
    }

    @Test
    fun onErrorContinueTest() {
        // given
        Flux.just(1, 2, 4, 0, 6, 12)
            .map { 12 / it }
            .onErrorContinue { t, u ->
                log.error { "error: ${t.message}, num: $u" }
            }.subscribe(
                {
                    log.info { "# onNext: $it" }
                },
                {
                    log.error { "# onError: $it" }
                }
            )
    }

    @Test
    fun retryTest() {
        // given
        val count = arrayOf(1)
        Flux.range(1, 3)
            .delayElements(Duration.ofSeconds(1))
            .map {
                if (it == 3 && count[0] == 1) {
                    count[0]++
                    TimeUnit.SECONDS.sleep(1)
                }
                it
            }
            .timeout(Duration.ofMillis(1500))
            .retry(1)
            .subscribe(
                {
                    log.info { "# onNext: $it" }
                },
                {
                    log.error { "# onError: $it" }
                },
                {
                    log.info { "# onComplete" }
                }
            )

        TimeUnit.SECONDS.sleep(7)
    }

    @Test
    fun elapsedTest() {
        // given
        Flux.range(1, 5)
            .delayElements(Duration.ofSeconds(1))
            .elapsed()
            .subscribe {
                log.info { "# onNext: $it" }
            }

        TimeUnit.SECONDS.sleep(6)
    }

    @Test
    fun apiRepeatAndElapsedTest() {
        // given
        val worldTimeUrl = UriComponentsBuilder.newInstance().scheme("http")
            .host("worldtimeapi.org")
            .port(80)
            .path("/api/timezone/Asia/Seoul")
            .build()
            .encode()
            .toUri()

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        // when
        Mono.defer {
            Mono.just(
                restTemplate.exchange(
                    worldTimeUrl,
                    HttpMethod.GET,
                    HttpEntity<String>(headers),
                    String::class.java
                )
            )
        }.repeat(4)
            .elapsed()
            .map {
                val jsonContext = JsonPath.parse(it.t2.body)
                val dateTime = jsonContext.read<String>("$.datetime")
                Tuples.of(dateTime, it.t1)
            }.subscribe(
                {
                    log.info { "now: ${it.t1}, elapsed: ${it.t2}" }
                },
                {
                    log.error { "# onError: $it" }
                },
                {
                    log.info { "# onComplete" }
                }
            )
    }

    @Test
    fun windowTest() {
        // given
        Flux.range(1, 11)
            .window(3)
            .flatMap {
                log.info { "=====================" }
                it
            }.subscribe(object : BaseSubscriber<Int>() {

                override fun hookOnSubscribe(subscription: Subscription) {
                    subscription.request(2)
                }

                override fun hookOnNext(value: Int) {
                    log.info { "# onNext: $value" }
                    request(2)
                }
            })
    }

    @Test
    fun bufferTest() {
        // given
        Flux.range(1, 95)
            .buffer(10)
            .subscribe {
                log.info { "# onNext: $it" }
            }

        Flux.range(1, 20)
            .map {
                if (it < 10) {
                    TimeUnit.MILLISECONDS.sleep(100)
                } else {
                    TimeUnit.MILLISECONDS.sleep(300)
                }
                it
            }.bufferTimeout(3, Duration.ofMillis(400))
            .subscribe {
                log.info { "# onNext: $it" }
            }
    }

    @Test
    fun refCountTest() {
        // given
        val publisher = Flux
            .interval(Duration.ofMillis(500))
            .publish()
            .refCount(1)

        val disposable = publisher.subscribe {
            log.info { "# subscriber 1: $it" }
        }

        TimeUnit.MILLISECONDS.sleep(2100)
        disposable.dispose()

        publisher.subscribe {
            log.info { "# subscriber 2: $it" }
        }

        TimeUnit.SECONDS.sleep(2500)
    }

    companion object {
        fun sayDefault(): Mono<String> {
            log.info { "# Say Hi" }
            return Mono.just("Hi")
        }

        fun getBooksFromCache(keyword: String): Flux<Book> {
            return Flux.fromIterable(
                getBooks()
            ).filter {
                it.name.contains(keyword)
            }.switchIfEmpty {
                Flux.error<RuntimeException>(NoSuchElementException("No such book"))
            }
        }

        fun getBooksFromDatabase(keyword: String): Flux<Book> {
            val books = getBooks().toMutableList()
            books.add(
                Book(
                    name = "webflux",
                    description = "reactive streams study",
                    author = "john",
                    alias = "reactive-streams",
                    price = 32000,
                    quantity = 120
                )
            )

            return Flux.fromIterable(
                books
            ).filter {
                it.name.contains(keyword)
            }.switchIfEmpty {
                Flux.error<RuntimeException>(NoSuchElementException("No such book"))
            }
        }

        private fun getBooks() = listOf(
            Book(
                name = "DDD",
                description = "Domain Driven Design",
                author = "junyoung",
                alias = "ddd-man",
                price = 35000,
                quantity = 200
            ),
            Book(
                name = "Spring",
                description = "Spring MVC",
                author = "jean",
                alias = "spring-man",
                price = 30000,
                quantity = 100
            )
        )
    }
}

data class Book(
    val name: String,
    val description: String,
    val author: String,
    val alias: String,
    val price: Int,
    val quantity: Int
)
