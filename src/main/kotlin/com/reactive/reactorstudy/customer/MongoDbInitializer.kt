package com.reactive.reactorstudy.config

import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.domain.ReactiveCustomerRepository
import com.reactive.reactorstudy.customer.domain.Telephone
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class MongoDbInitializer {

    @Autowired
    lateinit var reactiveCustomerRepository: ReactiveCustomerRepository

    companion object {
        val initialCustomers = listOf(
            Customer(1, "Kotlin"),
            Customer(2, "Spring"),
            Customer(3, "Microservice", Telephone("+44", "79007714"))
        )
    }

    @PostConstruct
    fun initData() {
        reactiveCustomerRepository.saveAll(initialCustomers)
    }
}
