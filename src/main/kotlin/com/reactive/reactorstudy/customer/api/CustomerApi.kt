package com.reactive.reactorstudy.customer.api

import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
class CustomerApi(
    private val customerService: CustomerService
) {

    @GetMapping(value = ["/customer/{id}"])
    fun getCustomer(@PathVariable id: Int): ResponseEntity<Mono<Customer>> {
        val customer = customerService.getCustomer(id)
        val status = if (customer == null) HttpStatus.NOT_FOUND else HttpStatus.OK
        return ResponseEntity(customer, status)
    }

    @GetMapping(value = ["/customers"])
    fun getCustomers(
        @RequestParam(required = false, defaultValue = "")
        nameFilter: String
    ) = customerService.searchCustomers(nameFilter)

    @PostMapping(value = ["/customer/"])
    fun createCustomer(@RequestBody customerMono: Mono<Customer>) =
        ResponseEntity(customerService.createCustomer(customerMono), HttpStatus.CREATED)
}
