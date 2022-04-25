package com.reactive.reactorstudy.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "account")
class Account(
    @Id
    val _id: String,
    val expiredDate: String,
    val customerId: String
)
