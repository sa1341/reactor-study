package com.reactive.reactorstudy.reactor.service

import com.reactive.reactorstudy.reactor.entity.BookEntity
import com.reactive.reactorstudy.reactor.repository.BookRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BookService {

    lateinit var bookRepository: BookRepository

    fun saveBook(book: BookEntity): Mono<BookEntity> {
        return verifyExistIsbn(book.isbn)
            .then(bookRepository.save(book))
    }

    private fun verifyExistIsbn(isbn: String): Mono<Void> {
        return bookRepository.findByIsbn(isbn)
            .flatMap {
                it?.let {
                    Mono.error(RuntimeException("Error"))
                } ?: Mono.empty()
            }
    }

    fun updateBook(book: BookEntity): Mono<BookEntity> {
        return findVerifiedBook(book.bookId)
            .map {
                it.copy(
                    titleKoran = book.titleKoran,
                    titleEnglish = book.titleEnglish,
                    description = book.description,
                    author = book.author
                )
            }.flatMap {
                bookRepository.save(it)
            }
    }

    fun findBook(bookId: Long) = findVerifiedBook(bookId)

    fun findBooks() = bookRepository.findAll().collectList()

    private fun findVerifiedBook(bookId: Long): Mono<BookEntity> {
        return bookRepository.findById(bookId)
            .switchIfEmpty(
                Mono.error(RuntimeException("Error"))
            )
    }
}
