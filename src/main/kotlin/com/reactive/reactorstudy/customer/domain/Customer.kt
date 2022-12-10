package com.reactive.reactorstudy.customer.domain

import org.springframework.data.mongodb.core.mapping.Document


@Document(collation = "Customers")
data class Customer(
    var id: Int = 0,
    var name: String = "",
    var telephone: Telephone? = null
)
