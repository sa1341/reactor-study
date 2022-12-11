package com.reactive.reactorstudy.customer

import com.reactive.reactorstudy.customer.api.CustomerApi
import com.reactive.reactorstudy.customer.domain.Customer
import com.reactive.reactorstudy.customer.service.CustomerService
import org.amshove.kluent.`should be null`
import org.amshove.kluent.`should not be null`
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.reset
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

@ExtendWith(SpringExtension::class)
@WebFluxTest(CustomerApi::class)
class CustomerControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var customerService: CustomerService


    @Test
    @DisplayName(value = "고객목록을 반환한다.")
    fun 고객목록을_반환한다() {
        // given
        given(customerService.getCustomer(1))
            .willReturn(Mono.just( Customer(1, "Kotlin")))

        // when
        val response = webTestClient.get()
            .uri("/customer/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .returnResult(Customer::class.java)
            .responseBody
            .blockLast()

        response.`should not be null`()

        // then
        println("result = ${response?.name}")
    }
}
