package com.reactive.reactorstudy.reactor.handler

import com.reactive.reactorstudy.reactor.entity.BookEntity
import com.reactive.reactorstudy.reactor.service.BookService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.net.URI
import java.time.LocalDateTime

@Component
class BookHandler(
    private val bookService: BookService
) {
    fun createBook(request: ServerRequest): Mono<ServerResponse> {
        return request.bodyToMono(BookDto.Post::class.java)
            .flatMap {
                bookService.saveBook(it.toBookEntity())
            }.flatMap {
                ServerResponse.created(URI.create("/v5/books/${it.bookId}"))
                    .build()
            }
    }
}

class BookDto {
    data class Post(
        val id: Long,
        val description: String,
        val titleKoran: String,
        val titleEnglish: String,
        val author: String,
        val isbn: String,
        val publishDate: String,
    ) {
        fun toBookEntity(): BookEntity {
            return BookEntity(
                bookId = id,
                titleKoran = titleKoran,
                titleEnglish = titleEnglish,
                description = description,
                author = author,
                isbn = isbn,
                publishDate = "2023-07-07",
                createdAt = LocalDateTime.now(),
                modifiedAt = LocalDateTime.now()
            )
        }
    }
}
