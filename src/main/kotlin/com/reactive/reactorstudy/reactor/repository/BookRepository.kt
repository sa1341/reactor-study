package com.reactive.reactorstudy.reactor.repository

import com.reactive.reactorstudy.reactor.entity.BookEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface BookRepository : ReactiveCrudRepository<BookEntity, Long> {
    fun findByIsbn(isbn: String): Mono<BookEntity?>
}
