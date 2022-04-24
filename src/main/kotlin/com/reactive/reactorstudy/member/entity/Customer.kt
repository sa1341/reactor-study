package com.reactive.reactorstudy.member.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef

class Customer {

    @Id
    var id: String? = null

    @DBRef(lazy = true)
    var accounts = mutableListOf<Account>()
}
