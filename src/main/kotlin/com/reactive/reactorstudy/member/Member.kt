package com.reactive.reactorstudy.member

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("members")
class Member(
    @Id
    val id: Long,
    var name: String,
    var age: Int
)
