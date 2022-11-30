package com.reactive.reactorstudy.customer.domain

data class Customer(
    val id: Int = 0,
    val name: String = "",
    var telephone: Telephone? = null
)
