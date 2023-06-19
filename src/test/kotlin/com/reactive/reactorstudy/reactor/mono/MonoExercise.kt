package com.reactive.reactorstudy.reactor.mono

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.function.Tuple2

private val log = KotlinLogging.logger {}
const val HEADER_AUTH_TOKEN = "authorization"

class MonoExercise {

    @Test
    fun deferTest() {
        // given
        Mono.defer {
            Mono.just(1)
        }.subscribe {
            log.debug { "next: $it" }
        }
    }

    @Test
    fun contextTest() {
        // given
        val mono = postBook(
            Mono.just(
                Book(
                    phoneNumber = "010-7900-7714",
                    name = "book",
                    author = "junyoung"
                )
            )
        ).contextWrite(Context.of(HEADER_AUTH_TOKEN, "eyJhGciOi"))

        // when
        mono.subscribe {
            log.info { "# onNext: $it" }
        }
    }

    private fun postBook(book: Mono<Book>): Mono<String> {
        val zip: Mono<Tuple2<Book, String>> = Mono
            .zip(
                book,
                Mono.deferContextual {
                    Mono.just(it.get(HEADER_AUTH_TOKEN))
                }
            )

        return zip.flatMap {
            val response = "POST the book(${it.t1.name}, ${it.t1.author}) with token: ${it.t2}"
            Mono.just(response)
        }
    }
}

data class Book(
    val phoneNumber: String,
    val name: String,
    val author: String
)
