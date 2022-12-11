package com.reactive.reactorstudy.customer.domain

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ReactiveCustomerRepository: ReactiveCrudRepository<Customer, Int> {
}
