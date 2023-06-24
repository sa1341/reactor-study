package com.reactive.reactorstudy.reactor.debugging

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Hooks
import reactor.core.scheduler.Schedulers
import java.util.concurrent.TimeUnit
import java.util.logging.Level

private val log = KotlinLogging.logger {}

class DebuggingExercise {

    private val fruits: Map<String, String> = createFruits()

    @Test
    fun debugTest() {
        Hooks.onOperatorDebug()

        // given
        Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
            .subscribeOn(Schedulers.boundedElastic())
            .publishOn(Schedulers.parallel())
            .map(String::lowercase)
            .map { it.substring(0, it.length - 1) }
            .map(fruits::get)
            .map { "맛있는 $it" }
            .subscribe(
                {
                    log.info { it }
                },
                {
                    log.error { "# onError: $it" }
                }
            )

        TimeUnit.MILLISECONDS.sleep(100)
    }

    @Test
    fun logTest() {
        // given
        Flux.fromArray(arrayOf("BANANAS", "APPLES", "PEARS", "MELONS"))
            .map(String::lowercase)
            .map { it.substring(0, it.length - 1) }
            .log("Fruit.Substring", Level.FINE)
            .map(fruits::get)
            .log("Fruit.get", Level.FINE)
            .subscribe(
                {
                    log.info { it }
                },
                {
                    log.error { "# onError: $it" }
                }
            )
    }

    @Test
    fun checkPointTest() {
        // given
        Flux.just(2, 4, 6, 8)
            .zipWith(Flux.just(1, 2, 3, 0)) { a: Int, b: Int -> a / b }
            .checkpoint()
            .map { it + 2 }
            .checkpoint()
            .subscribe(
                {
                    log.info { "# onNext: $it" }
                },
                {
                    log.error { log.error { "# onError: $it" } }
                }
            )
    }

    companion object {
        fun createFruits(): Map<String, String> {
            val fruits = mutableMapOf<String, String>()
            fruits["banana"] = "바나나"
            fruits["apple"] = "사과"
            fruits["pear"] = "배"
            fruits["grape"] = "포도"
            return fruits
        }
    }
}
