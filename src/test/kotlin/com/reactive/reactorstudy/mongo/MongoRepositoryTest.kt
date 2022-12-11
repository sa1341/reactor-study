package com.reactive.reactorstudy.mongo

import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.domain.ReactiveCustomerRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback

@SpringBootTest
class MongoRepositoryTest {

    @Autowired
    lateinit var reactiveCustomerRepository: ReactiveCustomerRepository

    @Test
    @DisplayName("Customer를 Mongo Db에 저장한다.")
    @Rollback(value = false)
    fun saveCustomerWithDocument() {

        // given
        val customer = Customer(1, "Kotlin")

        // when
        val savedCustomer = reactiveCustomerRepository.save(customer)

        println("Customer: $savedCustomer")

    }
}
