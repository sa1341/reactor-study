package com.reactive.reactorstudy.customer.service

import com.reactive.reactorstudy.customer.DataConfig
import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.domain.Telephone
import com.reactive.reactorstudy.global.exception.CustomerExistException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.util.concurrent.ConcurrentHashMap

@Service
class CustomerServiceImpl : CustomerService {

    companion object {
        val initialCustomers = arrayOf(
            Customer(1, "Kotlin")
            , Customer(2, "Spring")
            , Customer(3, "Microservice",  telephone = Telephone("+44", "79007714"))
        )
    }

    private val customers = ConcurrentHashMap<Int,
            Customer>(initialCustomers.associateBy(Customer::id))

    override fun getCustomer(id: Int) = customers[id]?.toMono() ?: Mono.empty()

    override fun searchCustomers(nameFilter: String): Flux<Customer> {
        return customers.filter {
            it.value.name.contains(nameFilter, true)
        }.map(Map.Entry<Int, Customer>::value).toFlux()
    }

    override fun createCustomer(customerMono: Mono<Customer>): Mono<*> {
        return customerMono.flatMap {
            if (customers[it.id] == null) {
                customers[it.id] = it
                it.toMono()
            } else {
                Mono.error(CustomerExistException("customer ${it.id} already exist"))
            }
        }
    }
}
