package com.reactive.reactorstudy.customer.handler

import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.service.CustomerService
import com.reactive.reactorstudy.global.exception.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.bodyToMono

@Component
class CustomHandler(
    private val customerService: CustomerService
) {

    fun get(serverRequest: ServerRequest) =
        customerService.getCustomer(serverRequest.pathVariable("id").toInt())
            .flatMap { ok().body(fromObject(it)) }
            .switchIfEmpty(status(HttpStatus.NOT_FOUND).build())

    fun search(serverRequest: ServerRequest) =
        ok().body(customerService.searchCustomers(serverRequest.queryParam("nameFilter")
            .orElse("")), Customer::class.java)

    fun create(serverRequest: ServerRequest) =
        customerService.createCustomer(serverRequest.bodyToMono()).flatMap {
            status(HttpStatus.CREATED).body(fromObject(it))
        }.onErrorResume(Exception::class.java) {
            badRequest().body(fromObject(ErrorResponse("error creating customer", it.message ?: "error")))
        }
}
