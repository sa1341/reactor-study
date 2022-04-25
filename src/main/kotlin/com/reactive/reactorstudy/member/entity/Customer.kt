package com.reactive.reactorstudy.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "customer")
class Customer(
    @Id
    val _id: String,
    var name: String,
)
