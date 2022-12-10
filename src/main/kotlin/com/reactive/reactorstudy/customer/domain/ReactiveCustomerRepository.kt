package com.reactive.reactorstudy.customer.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface CustomerRepository: ReactiveCrudRepository<Customer, Int> {
}
