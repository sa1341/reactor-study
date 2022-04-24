package com.reactive.reactorstudy.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collation = "account")
class Account(

    @Id
    val id: String,
    val customerId: String,
    var acno: String,
    var name: String,
)
