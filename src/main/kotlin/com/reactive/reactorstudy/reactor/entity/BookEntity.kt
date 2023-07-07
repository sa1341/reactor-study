package com.reactive.reactorstudy.reactor.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Id

data class BookEntity(
    @Id
    val bookId: Long,
    val titleKoran: String,
    val titleEnglish: String,
    val description: String,
    val author: String,
    val isbn: String,
    val publishDate: String,
    @CreatedDate
    val createdAt: LocalDateTime,
    @LastModifiedDate
    val modifiedAt: LocalDateTime
)
