package com.reactive.reactorstudy.member.repository

import com.reactive.reactorstudy.member.entity.Customer
import org.springframework.data.mongodb.repository.MongoRepository

interface CustomerRepository: MongoRepository<Customer, String> {
}
